/**
 * StringEncrypter.java
 * Copyright ? 2005 Neuragenix, Inc .  All rights reserved.
 * Date: 02/12/2005
 */
package neuragenix.security;

/**
 * Class used to encrypt and decrypt text.
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class StringEncrypter
{
	
	public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
	public static final String DES_ENCRYPTION_SCHEME = "DES";
	public static final String DEFAULT_ENCRYPTION_KEY = "This is the default NGX key used to encrypt and decrypt text.";
        public static final String NGX_ENCRYPTION_PREFIX = "(NGXEM)";
	
	private KeySpec	keySpec;
	private SecretKeyFactory keyFactory;
	private Cipher cipher;
	
	private static final String UNICODE_FORMAT = "UTF8";

        /** Default constructor
         */
	public StringEncrypter( String encryptionScheme ) throws EncryptionException
	{
                this( encryptionScheme, DEFAULT_ENCRYPTION_KEY );
	}

        /** Alternative constructor
         *  @param encryptionScheme: the encryption scheme
         *  @param encryptionKey: the encryption key
         */
	public StringEncrypter( String encryptionScheme, String encryptionKey )
			throws EncryptionException
	{

		if ( encryptionKey == null )
				throw new IllegalArgumentException( "encryption key was null" );
		if ( encryptionKey.trim().length() < 24 )
				throw new IllegalArgumentException(
						"encryption key was less than 24 characters" );

		try
		{
			byte[] keyAsBytes = encryptionKey.getBytes( UNICODE_FORMAT );

			if ( encryptionScheme.equals( DESEDE_ENCRYPTION_SCHEME) )
			{
				keySpec = new DESedeKeySpec( keyAsBytes );
			}
			else if ( encryptionScheme.equals( DES_ENCRYPTION_SCHEME ) )
			{
				keySpec = new DESKeySpec( keyAsBytes );
			}
			else
			{
				throw new IllegalArgumentException( "Encryption scheme not supported: "
													+ encryptionScheme );
			}

			keyFactory = SecretKeyFactory.getInstance( encryptionScheme );
			cipher = Cipher.getInstance( encryptionScheme );

		}
		catch (InvalidKeyException e)
		{
			throw new EncryptionException( e );
		}
		catch (UnsupportedEncodingException e)
		{
			throw new EncryptionException( e );
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new EncryptionException( e );
		}
		catch (NoSuchPaddingException e)
		{
			throw new EncryptionException( e );
		}

	}

        /** Encrypt text
         *  @param unencryptedString: the unencrypted string
         *  @return encrypted string
         *  @throws EncryptionException
         */
	public String encrypt( String unencryptedString ) throws EncryptionException
	{
                // if the string is null or empty, no encryption needs to be done
		if ( unencryptedString == null || unencryptedString.trim().length() == 0 )
                {
                        return unencryptedString;
                }
				

		try
		{
			SecretKey key = keyFactory.generateSecret( keySpec );
			cipher.init( Cipher.ENCRYPT_MODE, key );
			byte[] cleartext = unencryptedString.getBytes( UNICODE_FORMAT );
			byte[] ciphertext = cipher.doFinal( cleartext );

			BASE64Encoder base64encoder = new BASE64Encoder();
			return NGX_ENCRYPTION_PREFIX + base64encoder.encode( ciphertext );
		}
		catch (Exception e)
		{
			throw new EncryptionException( e );
		}
	}

        /** Decrypt text
         *  @param encryptedString: the encrypted string
         *  @return decrypted string
         *  @throws EncryptionException
         */
	public String decrypt( String encryptedString ) throws EncryptionException
	{
                // if the string is null or empty, no decryption needs to be done
		if ( encryptedString == null || encryptedString.trim().length() <= 0 )
                {
                        return encryptedString;
                }
				

		try
		{
                        encryptedString = encryptedString.substring(NGX_ENCRYPTION_PREFIX.length());
			SecretKey key = keyFactory.generateSecret( keySpec );
			cipher.init( Cipher.DECRYPT_MODE, key );
			BASE64Decoder base64decoder = new BASE64Decoder();
			byte[] cleartext = base64decoder.decodeBuffer( encryptedString );
			byte[] ciphertext = cipher.doFinal( cleartext );

			return bytes2String( ciphertext );
		}
		catch (Exception e)
		{
			throw new EncryptionException( e );
		}
	}

	private static String bytes2String( byte[] bytes )
	{
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < bytes.length; i++)
		{
			stringBuffer.append( (char) bytes[i] );
		}
		return stringBuffer.toString();
	}

	public static class EncryptionException extends Exception
	{
		public EncryptionException( Throwable t )
		{
			super( t );
		}
	}
}
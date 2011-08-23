/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.geno.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

import au.org.theark.geno.util.BitSet;


public abstract class BitUtil {
	public static ByteArrayInputStream bitsetToBlob(BitSet myBitSet, Connection con)
			throws SQLException {
		byte[] byteArray = myBitSet.toByteArray();
		ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
		return bis;
	}
	
	public static boolean[] longtoBitArray(long l) {
		int shift = 1; 
		        boolean[] buf = new boolean[64];
		        int charPos = 64;
		        int radix = 1 << shift;
		        long mask = radix - 1;
		        do {
		            buf[--charPos] = (l & mask) == 1L;
		            l >>>= shift;
		        } while (l != 0);
		       return buf;

	}
	
	
	public static ByteArrayInputStream bitSetToCompressedBlob(BitSet myBitSet, Connection con)  throws SQLException{
		byte[] byteArray = myBitSet.toByteArray();
		Deflater compressor = new Deflater();
	    compressor.setLevel(Deflater.BEST_COMPRESSION);
	    
	    // Give the compressor the data to compress
	    compressor.setInput(byteArray);
	    compressor.finish();
	    
	    // Create an expandable byte array to hold the compressed data.
	    // You cannot use an array that's the same size as the orginal because
	    // there is no guarantee that the compressed data will be smaller than
	    // the uncompressed data.
	    ByteArrayOutputStream bos = new ByteArrayOutputStream(byteArray.length);
	    
	    // Compress the data
	    byte[] buf = new byte[1024];
	    while (!compressor.finished()) {
	        int count = compressor.deflate(buf);
	        bos.write(buf, 0, count);
	    }
	    try {
	        bos.close();
	    } catch (IOException e) {
	    }
	    
	    // Get the compressed data
	    byte[] compressedData = bos.toByteArray();
	    ByteArrayInputStream bis = new ByteArrayInputStream(compressedData);
	
		
		return bis;
		
	
		
	}

	public static byte[] toByteArray(BitSet bits) {
		byte[] bytes = new byte[bits.length() / 8 + 1];
		for (int i = 0; i < bits.length(); i++) {
			if (bits.get(i)) {
				bytes[bytes.length - i / 8 - 1] |= 1 << (i % 8);
			}

		}
		return bytes;
	}

	public static BitSet blobToBitSet(Blob blob) throws SQLException {
		byte[] bytes = blob.getBytes(1, (int) blob.length());
		BitSet bitSet = BitSet.valueOf(bytes);

		return bitSet;
	}
	
	public static BitSet bitStreamtoBitSet(InputStream i, int nbits) throws IOException {
		int bytes = (int) (Math.ceil(nbits / 8.0) * 8); 
		byte[] bits = new byte[bytes];
		if (i.read(bits, 0, bytes) == bytes)
			return BitSet.valueOf(bits);
		else
			return null;
		}
	
	public static BitSet bitStreamtoMask(InputStream i, int nbits) throws IOException {
		int bytes = (int) (Math.ceil(nbits / 8.0) * 8); 
		byte[] bits = new byte[bytes];
		if (i.read(bits, 0, bytes) == bytes)
			return  BitSet.valueOf(bits);
		else
			return null;
		}
	
	
	
	public static BitSet CompressedBlobToBitSet(Blob blob) throws SQLException {
		byte[] bytes = blob.getBytes(1, (int) blob.length());
		// Create the decompressor and give it the data to compress
	    Inflater decompressor = new Inflater();
	    decompressor.setInput(bytes);
	    
	    // Create an expandable byte array to hold the decompressed data
	    ByteArrayOutputStream bos = new ByteArrayOutputStream(bytes.length);
	    
	    // Decompress the data
	    byte[] buf = new byte[1024];
	    while (!decompressor.finished()) {
	        try {
	            int count = decompressor.inflate(buf);
	            bos.write(buf, 0, count);
	        } catch (DataFormatException e) {
	        }
	    }
	    try {
	        bos.close();
	    } catch (IOException e) {
	    }
	    
	    // Get the decompressed data
	    byte[] decompressedData = bos.toByteArray();
	    BitSet bitSet = BitSet.valueOf(decompressedData);

		return bitSet;
		
		
	}
	
	

	
	public static BitSet fromByteArray(byte[] bytes) {
		BitSet bits = new BitSet();
		for (int i = 0; i < bytes.length * 8; i++) {
			if ((bytes[bytes.length - i / 8 - 1] & (1 << (i % 8))) > 0) {
				bits.set(i);
			}
		}
		return bits;
	}


}

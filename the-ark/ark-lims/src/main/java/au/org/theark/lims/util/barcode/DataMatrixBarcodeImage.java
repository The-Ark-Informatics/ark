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
package au.org.theark.lims.util.barcode;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.krysalis.barcode4j.impl.datamatrix.DataMatrixBean;
import org.krysalis.barcode4j.impl.datamatrix.SymbolShapeHint;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class creates a 2D DataMatrix barcode bitmap, enhanced by custom elements.
 * 
 * @author cellis
 */
public class DataMatrixBarcodeImage extends NonCachingImage {
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 3235603243807828600L;
	private static final Logger	log					= LoggerFactory.getLogger(DataMatrixBarcodeImage.class);
	private static int				dpi					= 200;
	private boolean					antiAlias			= true;
	private int							orientation			= 0;

	/**
	 * Create a new DataMatrixBarcode of the specified barcodeString
	 * 
	 * @param id
	 * @param barcodeString
	 */
	public DataMatrixBarcodeImage(String id, final IModel<String> barcodeStringModel) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
		setImageResource(getDataMatrixBarcodeImageResource(barcodeStringModel));
	}

	/**
	 * Create a new DataMatrixBarcode of the specified barcodeString with specified parameters
	 * 
	 * @param id
	 * @param barcodeString
	 * @param dpi
	 * @param anitAlias
	 * @param orientation
	 */
	public DataMatrixBarcodeImage(String id, final String barcodeString, int dpi, boolean anitAlias, int orientation) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
		DataMatrixBarcodeImage.dpi = dpi;
		this.antiAlias = anitAlias;
		this.orientation = orientation;
	}

	/**
	 * @return Gets shared image component
	 */
	public static DynamicImageResource getDataMatrixBarcodeImageResource(final IModel<String> barcodeStringModel) {
		return new DynamicImageResource() {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected byte[] getImageData(Attributes attributes) {
				String barcodeString = barcodeStringModel.getObject();
				final BufferedImage image = generateBufferedImage(barcodeString);
				return toImageData(image);
			}
		};
	}

	/**
	 * Creates a BufferedImage of the specified string as a 2D DataMatrix barcode
	 * 
	 * @param msg
	 * @return A Bufferedimage of the 2D DataMatrix barcode
	 * @throws IOException
	 */
	public static BufferedImage generateBufferedImage(String barcodeString) {
		// Create the barcode bean
		DataMatrixBean bean = new DataMatrixBean();

		// Configure the barcode generator
		bean.setModuleWidth(UnitConv.in2mm(10.0f / dpi)); // makes a dot/module exactly ten pixels
		bean.doQuietZone(false);
		bean.setShape(SymbolShapeHint.FORCE_SQUARE);

		boolean antiAlias = true;
		int orientation = 0;
		// Set up the canvas provider to create a monochrome bitmap
		BitmapCanvasProvider canvas = new BitmapCanvasProvider(dpi, BufferedImage.TYPE_BYTE_BINARY, antiAlias, orientation);

		// Generate the barcode
		bean.generateBarcode(canvas, barcodeString);

		// Signal end of generation
		try {
			canvas.finish();
		}
		catch (IOException e) {
			log.error(e.getMessage());
		}

		// Generate the bufferedImage
		BufferedImage bufferedImage = canvas.getBufferedImage();
		return bufferedImage;
	}

}
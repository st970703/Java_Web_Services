package nz.ac.auckland.aws;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.MultipleFileDownload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;


/**
 * Simple AWS S3 client.
 *
 */
public class AWSClient {

	// AWS S3 access credentials for concert images.
	private static final String AWS_ACCESS_KEY_ID = "AKIAIDYKYWWUZ65WGNJA";
	private static final String AWS_SECRET_ACCESS_KEY = "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";

	// Name of the S3 bucket that stores images.
	private static final String AWS_BUCKET = "concert.aucklanduni.ac.nz";

	// Download directory - a directory named "images" in the user's home
	// directory.
	private static final String FILE_SEPARATOR = System
			.getProperty("file.separator");
	private static final String USER_DIRECTORY = System
			.getProperty("user.home");
	private static final String DOWNLOAD_DIRECTORY = USER_DIRECTORY
			+ FILE_SEPARATOR + "images";

	private static Logger _logger = LoggerFactory.getLogger(AWSClient.class);

	public static void main(String[] args) {

		// Create download directory if it doesn't already exist.
		File downloadDirectory = new File(DOWNLOAD_DIRECTORY);
		downloadDirectory.mkdir();

		// Create an AmazonS3 object that represents a connection with the
		// remote S3 service.
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
				AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
		AmazonS3 s3 = AmazonS3ClientBuilder
				.standard()
				.withRegion(Regions.AP_SOUTHEAST_2)
				.withCredentials(
						new AWSStaticCredentialsProvider(awsCredentials))
				.build();

		// Find images names stored in S3.
		List<String> imageNames = getImageNames(s3);

		// Download the images.
		download(s3, imageNames);
	}

	/**
	 * Finds image names stored in a bucket named AWS_BUCKET.
	 *
	 * @param s3 the AmazonS3 connection.
	 *
	 * @return a List of images names.
	 *
	 */
	private static List<String> getImageNames(AmazonS3 s3) {

		ObjectListing ol = s3.listObjects(AWS_BUCKET);

		List<S3ObjectSummary> objects = ol.getObjectSummaries();

		List<String> imgList = new ArrayList<String>();

		for (S3ObjectSummary os: objects) {
			System.out.println(os.getKey());
			imgList.add(os.getKey());
		}


		return imgList;
	}

	/**
	 * Downloads images in the bucket named AWS_BUCKET.
	 *
	 * @param s3 the AmazonS3 connection.
	 *
	 * @param imageNames the named images to download.
	 *
	 */
	private static void download(AmazonS3 s3, List<String> imageNames) {
		TransferManager xfer_mgr = TransferManagerBuilder
				.standard()
				.withS3Client(s3)
				.build();

		try {
			for (String str: imageNames) {
				File file = new File(str);

				Download xfer = xfer_mgr.download(AWS_BUCKET, str, file);
			}

		} catch (AmazonServiceException e) {
			System.err.println(e.getErrorMessage());
			System.exit(1);
		}

		try {
			// waste time
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		xfer_mgr.shutdownNow();
	}
}

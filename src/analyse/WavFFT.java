package analyse;

import org.jtransforms.fft.DoubleFFT_1D;

import WavFile.WavFile;
import WavFile.WavFileException;

import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * FFT/wav analysis class for the Analyse application.
 * 
 * Made in January - April 2016, as part of the 
 * project DI14 - Analyse de la voix, in Polytech' Tours.
 * 
 * @author Dimitris Kokkonis
 *
 */
public class WavFFT {
	
	// WavFile object to manipulate the raw file
	private WavFile wavFile;
	
	/**
	 * Constructor method for WavFFT
	 * 
	 * @param file : wav file to analyze
	 * @throws IOException
	 * @throws WavFileException
	 */
	WavFFT(File file) throws IOException, WavFileException
	{
		// just open the wav file
		wavFile = WavFile.openWavFile(file);
	}
	
	
	/**
	 * method to close the wav file we opened
	 * 
	 * @throws IOException
	 */
	void close() throws IOException
	{
		wavFile.close();
	}
	
	
	/**
	 * method to get all the data from the file
	 * 
	 * @return double[] : the data read from the wav file
	 * @throws IOException
	 * @throws WavFileException
	 */
	double[] getWavData() throws IOException, WavFileException
	{
		// an array, whose length is equal to the number of frames times the number of channels
		double[] buffer = new double[(int) (wavFile.getNumFrames() * wavFile.getNumChannels())];
		
		// read the entire wav file at once
		wavFile.readFrames(buffer, (int) wavFile.getNumFrames());

		// return the array of data
        return buffer;
	}
	
	
	/**
	 * method to perform a real FFT on wav file data
	 * 
	 * @return double[] : the FFT'd data
	 * @throws IOException
	 * @throws WavFileException
	 */
	double[] RealFFT() throws IOException, WavFileException
	{
		// get the data using the method we made before
		double[] data_to_fft = getWavData();
		
		/* get the number of frames, our files should be
		 * short so we're casting to int
		 */
		int frames = (int) wavFile.getNumFrames();
		
		// perform the FFT on the raw data
		DoubleFFT_1D fft = new DoubleFFT_1D(frames);
		fft.realForward(data_to_fft);
		
		// make an array to hold the frequency data
		double[] final_data = new double[frames/2];
		// iterate through the FFT'd data
        for(int i = 0; i < (frames/2); i+=2){
        	/* each point on the frequency array is
        	 * the module of the complex number stored
        	 * in the FFT'd data
        	 * 
        	 * since it's stored as data[i] = Re, data[i+1] = Im
        	 * we'll do final[i] = sqrt(data[i]^2 + data[i+1]^2),
        	 * which is basically the module of a complex number
        	 */
        	final_data[i/2] = Math.sqrt(data_to_fft[i] * data_to_fft[i] + data_to_fft[i+1] * data_to_fft[i+1]);
        }
        
        // return the frequency data
        return final_data;
	}
	
	
	/**
	 * method to write frequency data to file
	 * 
	 * @param out : a file to write the data to
	 * @throws IOException
	 * @throws WavFileException
	 */
	void WriteToFile(File out) throws IOException, WavFileException
	{
		/* we'll need the duration (in seconds) of the file
		 * which is equal to (frames)/(sample rate)
		 */
		int sec = (int) (wavFile.getNumFrames() / wavFile.getSampleRate());
		
		// a PrintWriter var to print the numbers to the file
		PrintWriter print_out = new PrintWriter(out);
		
		// perform the fft
		double[] data_to_file = RealFFT();
		
		// iterate through the numbers and print them on the file
		for(int i = 0; i < data_to_file.length; i += sec){
			print_out.println(data_to_file[i]);
		}
		
		// close the PrintWriter
		print_out.close();
	}
	
}

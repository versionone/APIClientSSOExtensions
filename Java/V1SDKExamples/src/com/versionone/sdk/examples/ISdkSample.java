package com.versionone.sdk.examples;

public interface ISdkSample {
    
	/**
	 * Set attributes from Commandline
	 * @param args
	 */
	public void commandLineArguments(String[] args);

	/**
	 * Load data from a configuration file.
	 */
	public void loadConfig();
	
	/**
	 * Connect to VersionOne
	 */
	public abstract void connect();
	
	/**
	 * Run the example
	 */
	public abstract void run();

}

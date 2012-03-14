package com.versionone.sdk.examples;

public class Program {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        ISdkSample example = new SSOExample();
        example.loadConfig();
        example.commandLineArguments(args);
        example.connect();
        example.run();
	}

}

namespace V1SSOExample
{
    /// <summary>
    /// Base class for all SDK examples.  
    /// Provides methods for accessing common SDK components
    /// </summary>
    internal interface ISdkSampleBase
    {
        /// <summary>
        /// Set attributes from Commandline
        /// </summary>
        /// <param name="args"></param>
        void CommandLineArguments(string[] args);

        /// <summary>
        /// Load data from a configuration file.
        /// </summary>
        void LoadConfig();

        /// <summary>
        /// Connect to VersionOne
        /// </summary>
        void Connect();        

        /// <summary>
        /// Run the example
        /// </summary>
        void Run();

    }
}
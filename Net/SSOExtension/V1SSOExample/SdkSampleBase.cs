namespace V1SSOExample
{
    /// <summary>
    /// Interface that SDK examples need to implement
    /// </summary>
    internal interface ISdkSample
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
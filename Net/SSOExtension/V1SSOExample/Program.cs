namespace V1SSOExample
{
    class Program
    {
        static void Main(string[] args)
        {
            ISdkSampleBase example = new SsoExample();
            example.LoadConfig();
            example.CommandLineArguments(args);
            example.Connect();
            example.Run();
        }
    }
}

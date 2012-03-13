namespace VersionOne.SDK.APIClient.SSOExtension
{
    public interface IVersionOneSsoConfiguration
    {
        string ServerUrl { get; }

        string Username { get; }

        string Password { get; }

        bool IntegratedAuth { get; }

        string IdpUrl { get; }

        string IdpResponseParser { get; }

        string SpResponseParser { get; }

    }
}

namespace VersionOne.SDK.APIClient.SSOExtension
{
    /// <summary>
    /// The configuration requirements for SSO connectivity
    /// </summary>
    public interface IVersionOneSsoConfiguration
    {
        /// <summary>
        /// VersionOne Server URL
        /// </summary>
        string ServerUrl { get; }

        /// <summary>
        /// VersionOne Username
        /// In SSO, if your IDP uses integrated auth, you may not need to provide this information
        /// </summary>
        string Username { get; }

        /// <summary>
        /// Password for VersionOne user
        /// In SSO, if your IDP uses integrated auth, you may not need to provide this information
        /// </summary>
        string Password { get; }

        /// <summary>
        /// Does the server support integrated auth.
        /// In SSO, set this to True if your IDP uses integrated auth
        /// </summary>
        bool IntegratedAuth { get; }

        /// <summary>
        /// This is the URL to access the Identity Provider
        /// </summary>
        string IdpUrl { get; }

        /// <summary>
        /// Name of the class that will parse the response from the Identity Provider
        /// </summary>
        string IdpResponseParser { get; }

        /// <summary>
        /// Name of hte class that will parse the response from the Service Provider
        /// </summary>
        string SpResponseParser { get; }
    }
}

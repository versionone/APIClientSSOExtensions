using System.IO;

namespace VersionOne.SDK.APIClient.SSOExtension
{
    /// <summary>
    /// Interface that a SamlResponseParser needs to implement
    /// </summary>
    public interface IResponseParser
    {
        /// <summary>
        /// Post the SAML response to this URL
        /// </summary>
        string PostUrl { get; }

        /// <summary>
        /// This is the SAML response to post
        /// </summary>
        string SamlResponse { get; }

        /// <summary>
        /// SAML RelayState
        /// </summary>
        string RelayState { get; }

        /// <summary>
        /// This is the UriPartial.Authority part of the site that sent us the SAML response. 
        /// This is important when the PostsUrl is relative.
        /// </summary>
        string UrlAuthority { set; }

        /// <summary>
        /// Create the SAML Response from a stream
        /// </summary>
        /// <param name="stream">contains SAML response</param>
        void Load(Stream stream);

        /// <summary>
        /// Return the form data necessary to take the next step.
        /// </summary>
        /// <returns></returns>
        FormData FormData { get; }
    }
}

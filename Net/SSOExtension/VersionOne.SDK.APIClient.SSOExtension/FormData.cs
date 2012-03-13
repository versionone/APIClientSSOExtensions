using System;
using System.Collections;
using System.Web;

namespace VersionOne.SDK.APIClient.SSOExtension
{
    public abstract class FormData
    {
        private readonly ArrayList _formData = new ArrayList();

        public void Add(string key, string value)
        {
            _formData.Add(string.Format("{0}={1}", key, HttpUtility.UrlEncode(value)));
        }

        public void Add(string key)
        {
            _formData.Add(string.Format("{0}=", key));
        }

        public string ToPostData()
        {
            return String.Join("&", (String[])_formData.ToArray(typeof(string)));
        }

        /// <summary>
        /// Identity Providers have different field names for credentials.   
        /// This abstraction allows the code to make one call to set credentials. 
        /// </summary>
        /// <param name="username"></param>
        /// <param name="password"></param>
        public abstract void SetCredentials(string username, string password);
    }
}
import makeRequest from 'request';

export const request = {
    /**
     * Set the base that all URLs will be relative to
     * @param {String} base The URL base
     */
    setUrlBase: function(base) {
        this._serverBase = base;
    },

    /**
     * Get the response from the last request, if there is one
     * @return {Object} the last response received
     */
    lastResponse: function() {
        return this._lastResponse;
    },

    /**
     * Reset the last response received
     */
    reset: function() {
        delete this._lastResponse;
    },

    /**
     * Make a POST request, providing some form data to send
     * @param {String} url The URL to POST to, relative to the URL base specified
     * @param {Object} formData the Form Data to POST
     * @return {Promise} a promise for the result of the request
     */
    post: function(url, formData) {
        return new Promise((resolve, reject) => {
            makeRequest({
                method: 'POST',
                uri: `${this._serverBase}${url}`,
                form: formData
            }, (error, response, body) => {
                if (error) {
                    reject(error);
                } else {
                    this._lastResponse = {
                        body: JSON.parse(body),
                        headers: response.headers,
                        statusCode: response.statusCode
                    };
                    resolve(this._lastResponse);
                }
            });
        });
    }
};

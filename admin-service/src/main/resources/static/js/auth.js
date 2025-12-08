// Global fetch interceptor to add JWT from localStorage
(function() {
    const originalFetch = window.fetch;
    window.fetch = function(url, options = {}) {
        const token = localStorage.getItem('jwt-token');
        if (token) {
            options.headers = options.headers || {};
            options.headers['Authorization'] = 'Bearer ' + token;
        }
        return originalFetch(url, options);
    };
})();


(ns app.contents.backend.handler.routes
    (:require [http.api     :as http]
              [mongo-db.api :as mongo-db]
              [x.user.api   :as x.user]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-content
  ; @param (map) request
  ;
  ; @return (map)
  [request]
  (let [content-id (http/request->path-param request :content-id)]
       (if-let [{:content/keys [body visibility]} (mongo-db/get-document-by-id "contents" content-id)]
               (case visibility :private (if (x.user/request->authenticated? request)
                                             (http/map-wrap   {:body body})
                                             (http/error-wrap {:body :access-denied :status 403}))
                                :public      (http/map-wrap   {:body body}))
               (http/error-wrap {:error-message :content-not-found :status :404}))))

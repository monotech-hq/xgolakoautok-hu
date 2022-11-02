
(ns app.contents.backend.handler.routes
    (:require [mongo-db.api       :as mongo-db]
              [server-fruits.http :as http]
              [x.server-user.api  :as x.user]))

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


(ns app.contents.backend.handler.helpers
    (:require [app.common.backend.api :as common]
              [candy.api              :refer [return]]
              [mongo-db.api           :as mongo-db]
              [vector.api             :as vector]
              [x.user.api             :as x.user]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-content-body
  ; @param (map) request
  ; @param (namespaced map)
  ;  {:content/id (string)}
  ;
  ; @usage
  ;  (get-content-body {...} {:content/id "my-content"})
  ;
  ; @return (string)
  [request {:content/keys [id]}]
  (if-let [{:content/keys [body visibility]} (mongo-db/get-document-by-id "contents" id)]
          (case visibility :private (if (x.user/request->authenticated? request)
                                        (return body))
                           :public      (return body))))

(defn fill-data
  ; @param (map) request
  ; @param (map or maps in vector) n
  ;
  ; @example
  ;  (fill-data {...} {:my-content {:content/id "my-content"}})
  ;  =>
  ;  {:my-content {:content/id "my-content" :content/body "My content"}}
  ;
  ; @return (map)
  [request n]
  (letfn [(f [result k v]
             (if-let [content-id (:content/id v)]
                     (assoc-in result [k :content/body] (get-content-body request v))
                     (assoc-in result [k] v)))]
         (cond (vector? n) (vector/->items n #(fill-data request %))
               (map?    n) (reduce-kv f {} n))))

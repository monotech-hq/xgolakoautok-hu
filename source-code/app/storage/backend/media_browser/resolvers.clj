
(ns app.storage.backend.media-browser.resolvers
    (:require [app.common.backend.api                            :as common]
              [app.storage.backend.capacity-handler.side-effects :as capacity-handler.side-effects]
              [com.wsscode.pathom3.connect.operation             :refer [defresolver]]
              [engines.item-browser.api                          :as item-browser]
              [mongo-db.api                                      :as mongo-db]
              [pathom.api                                        :as pathom]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-f
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (namespaced map)
  [env resolver-props]
  (let [item-id    (pathom/env->param env :item-id)
        projection (common/get-document-projection :media)]
       (if-let [media-item (mongo-db/get-document-by-id "storage" item-id projection)]
               (if-let [capacity-details (capacity-handler.side-effects/get-capacity-details)]
                       (merge media-item capacity-details)))))

(defresolver get-item
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (map)
             ;  {:storage.media-browser/get-item (namespaced map)}
             [env resolver-props]
             {:storage.media-browser/get-item (get-item-f env resolver-props)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-items-f
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) resolver-props
  ;
  ; @return (map)
  ;  {:document-count (integer)
  ;   :documents (namespaced maps in vector)}
  [env _]
  (let [get-pipeline   (item-browser/env->get-pipeline   env :storage.media-browser)
        count-pipeline (item-browser/env->count-pipeline env :storage.media-browser)]
       {:items          (mongo-db/get-documents-by-pipeline   "storage" get-pipeline)
        :all-item-count (mongo-db/count-documents-by-pipeline "storage" count-pipeline)}))

(defresolver get-items
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:contents.content-lister/get-items (map)
             ;    {:document-count (integer)
             ;     :documents (namespaced maps in vector)}}
             [env resolver-props]
             {:storage.media-browser/get-items (get-items-f env resolver-props)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-item get-items])

(pathom/reg-handlers! ::handlers HANDLERS)

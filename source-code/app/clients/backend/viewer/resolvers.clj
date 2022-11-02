
(ns app.clients.backend.viewer.resolvers
    (:require [app.clients.backend.viewer.helpers    :as viewer.helpers]
              [app.common.backend.api                :as common]
              [com.wsscode.pathom3.connect.operation :refer [defresolver]]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-f
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (namespaced map)}
  [env _]
  ; XXX#7601
  (let [item-id    (pathom/env->param env :item-id)
        projection (common/get-document-projection :client)]
       (if-let [client-item (mongo-db/get-document-by-id "clients" item-id projection)]
               (viewer.helpers/client-item<-name-field env client-item))))

(defresolver get-item
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:clients.viewer/get-item (namespaced map)}
             [env resolver-props]
             {:clients.viewer/get-item (get-item-f env resolver-props)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-item])

(pathom/reg-handlers! ::handlers HANDLERS)

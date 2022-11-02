
(ns app.models.backend.viewer.resolvers
    (:require [app.common.backend.api                :as common]
              [com.wsscode.pathom3.connect.operation :refer [defresolver]]
              [engines.item-viewer.api               :as item-viewer]
              [mid-fruits.candy                      :refer [return]]
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
  (let [item-id    (pathom/env->param env :item-id)
        projection (common/get-document-projection :model)]
       (mongo-db/get-document-by-id "models" item-id projection)))

(defresolver get-item
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:models.viewer/get-item (namespaced map)}
             [env resolver-props]
             {:models.viewer/get-item (get-item-f env resolver-props)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-item])

(pathom/reg-handlers! ::handlers HANDLERS)

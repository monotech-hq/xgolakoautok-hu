
(ns app.rental-vehicles.backend.editor.resolvers
    (:require [app.common.backend.api                :as common]
              [com.wsscode.pathom3.connect.operation :refer [defresolver]]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-f
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (namespaced map)
  [env _]
  (let [item-id    (pathom/env->param env :item-id)
        projection (common/get-document-projection :vehicle)]
       (mongo-db/get-document-by-id "rental_vehicles" item-id projection)))

(defresolver get-item
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (map)
             ;  {:rental-vehicles.editor/get-item (namespaced map)}
             [env resolver-props]
             {:rental-vehicles.editor/get-item (get-item-f env resolver-props)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-item])

(pathom/reg-handlers! ::handlers HANDLERS)

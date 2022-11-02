
(ns app.packages.backend.editor.resolvers
    (:require [app.common.backend.api                :as common]
              [app.packages.backend.handler.helpers  :as handler.helpers]
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
        projection (common/get-document-projection :package)]
       (if-let [item (mongo-db/get-document-by-id "packages" item-id projection)]
               (let [automatic-price (handler.helpers/get-package-price item-id)]
                    (assoc item :package/automatic-price automatic-price)))))

(defresolver get-item
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:packages.editor/get-item (namespaced map)}
             [env resolver-props]
             {:packages.editor/get-item (get-item-f env resolver-props)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-item])

(pathom/reg-handlers! ::handlers HANDLERS)

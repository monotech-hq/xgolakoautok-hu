
(ns app.types.backend.lister.resolvers
    (:require [app.common.backend.api                :as common]
              [com.wsscode.pathom3.connect.operation :refer [defresolver]]
              [engines.item-lister.api               :as item-lister]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-items-f
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (map)
  ;  {:document-count (integer)
  ;   :documents (namespaced maps in vector)}
  [env _]
  (let [get-pipeline   (item-lister/env->get-pipeline   env :types.lister)
        count-pipeline (item-lister/env->count-pipeline env :types.lister)]
       {:items          (mongo-db/get-documents-by-pipeline   "types" get-pipeline)
        :all-item-count (mongo-db/count-documents-by-pipeline "types" count-pipeline)}))

(defresolver get-items
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:types.lister/get-items (map)
             ;    {:document-count (integer)
             ;     :documents (namespaced maps in vector)}}
             [env resolver-props]
             {:types.lister/get-items (get-items-f env resolver-props)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-items])

(pathom/reg-handlers! ::handlers HANDLERS)

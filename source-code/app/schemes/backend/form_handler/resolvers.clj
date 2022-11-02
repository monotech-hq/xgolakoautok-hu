
(ns app.schemes.backend.form-handler.resolvers
    (:require [app.common.backend.api                :as common]
              [com.wsscode.pathom3.connect.operation :refer [defresolver]]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-scheme-form-f
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (namespaced map)
  ;  {:pathom/target-path (vector)
  ;   :pathom/target-value (namespaced map)}
  [env _]
  (let [scheme-id  (pathom/env->param env :scheme-id)
        projection (common/get-document-projection :vehicle)]
       {:pathom/target-path  [:schemes :form-handler/scheme-forms scheme-id]
        :pathom/target-value (mongo-db/get-document-by-query "schemes" {:scheme/scheme-id scheme-id} projection)}))

(defresolver get-scheme-form
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:schemes.form-handler/get-scheme-form (namespaced map)
             ;   {:pathom/target-path (vector)}
             ;    :pathom/target-value (namespaced map)}
             [env resolver-props]
             {:schemes.form-handler/get-scheme-form (get-scheme-form-f env resolver-props)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-scheme-form])

(pathom/reg-handlers! ::handlers HANDLERS)

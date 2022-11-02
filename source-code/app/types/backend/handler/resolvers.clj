
(ns app.types.backend.handler.resolvers
    (:require [app.common.backend.api                :as common]
              [app.types.backend.handler.helpers     :as handler.helpers]
              [com.wsscode.pathom3.connect.operation :refer [defresolver]]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-model-name-f
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (namespaced map)
  ;  {:pathom/target-path (vector)
  ;   :pathom/target-value (string)}
  [env _]
  ; XXX#7602
  {:pathom/target-path  [:types :handler/model-item :name]
   :pathom/target-value (handler.helpers/get-model-name env)})

(defresolver get-model-name
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:types.handler/get-model-name (namespaced map)}
             [env resolver-props]
             ; XXX#7602
             ; A típust tartalmazó modell neve szükséges a kliens-oldalon megjelenő breadcrumbs menühöz!
             {:types.handler/get-model-name (get-model-name-f env resolver-props)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-model-name])

(pathom/reg-handlers! ::handlers HANDLERS)

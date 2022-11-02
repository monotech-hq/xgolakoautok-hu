
(ns app.price-quotes.backend.handler.resolvers
    (:require [app.price-quotes.backend.handler.helpers :as handler.helpers]
              [com.wsscode.pathom3.connect.operation    :refer [defresolver]]
              [pathom.api                               :as pathom]
              [templates.price-quotes.blank.api         :as blank]
              [tools.pdf-generator.api                  :as pdf-generator]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-pdf-f
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (string)
  [env _]
  (let [page-props     (handler.helpers/env->page-props     env)
        template-props (handler.helpers/env->template-props env)
        template       (blank/template template-props)]
       (try (pdf-generator/generate-base64-pdf! template page-props)
            (catch Exception e (println e)))))

(defresolver download-pdf
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:price-quotes.handler/download-pdf (string)}
             [env resolver-props]
             {:price-quotes.handler/download-pdf (download-pdf-f env resolver-props)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [download-pdf])

(pathom/reg-handlers! ::handlers HANDLERS)

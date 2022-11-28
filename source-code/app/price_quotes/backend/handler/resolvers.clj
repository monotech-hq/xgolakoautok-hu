
(ns app.price-quotes.backend.handler.resolvers
    (:require [app.price-quotes.backend.handler.helpers :as handler.helpers]
              [com.wsscode.pathom3.connect.operation    :refer [defresolver]]
              [pathom.api                               :as pathom]
              [templates.price-quotes.blank.api         :as blank]
              [tools.hiccuptopdf.api                    :as hiccuptopdf]
              [x.user.api                               :as x.user]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-pdf-f
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) resolver-props
  ;
  ; @return (string)
  [{:keys [request] :as env} _]
  (if (x.user/request->authenticated? request)
      (let [page-props     (handler.helpers/env->page-props     env)
            template-props (handler.helpers/env->template-props env)
            template       (blank/template template-props)]
           (try (hiccuptopdf/generate-base64-pdf! template page-props)
                (catch Exception e (println e))))))

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

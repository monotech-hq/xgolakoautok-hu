
(ns app.website-content.backend.editor.mutations
    (:require [app.common.backend.api                     :as common]
              [app.website-content.backend.handler.config :as handler.config]
              [candy.api                                  :refer [return]]
              [com.wsscode.pathom3.connect.operation      :as pathom.co :refer [defmutation]]
              [io.api                                     :as io]
              [pathom.api                                 :as pathom]
              [x.user.api                                 :as x.user]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-content-f
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) mutation-props
  ;  {:content (map)}
  ;
  ; @return (map)
  [{:keys [request]} {:keys [content]}]
  (if (x.user/request->authenticated? request)
      (let [content (common/updated-edn-prototype request content)]
           (io/write-edn-file! handler.config/WEBSITE-CONTENT-FILEPATH content)
           (return content))))

(defmutation save-content!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:content (map)}
             ;
             ; @return (map)
             [env mutation-props]
             {::pathom.co/op-name 'website-content.editor/save-content!}
             (save-content-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [save-content!])

(pathom/reg-handlers! ::handlers HANDLERS)

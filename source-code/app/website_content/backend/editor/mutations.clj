
(ns app.website-content.backend.editor.mutations
    (:require [app.website-content.backend.handler.config :as handler.config]
              [com.wsscode.pathom3.connect.operation      :as pathom.co :refer [defmutation]]
              [io.api                                     :as io]
              [mid-fruits.candy                           :refer [return]]
              [pathom.api                                 :as pathom]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-content-f
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:content (map)}
  ;
  ; @return (map)
  [_ {:keys [content]}]
  (io/write-edn-file! handler.config/WEBSITE-CONTENT-FILEPATH content)
  (return content))

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

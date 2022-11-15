
(ns app.schemes.frontend.field-editor.subs
    (:require [app.schemes.frontend.form-handler.subs :as form-handler.subs]
              [re-frame.api                           :refer [r]]
              [x.components.api                       :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-resolved-field-name
  ; @param (keyword) scheme-id
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ scheme-id field-id]]
  (let [{:field/keys [name]} (r form-handler.subs/get-scheme-field db scheme-id field-id)]
       (r x.components/get-metamorphic-value db {:content name})))

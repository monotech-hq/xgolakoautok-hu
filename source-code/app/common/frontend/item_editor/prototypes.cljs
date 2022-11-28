
(ns app.common.frontend.item-editor.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; @param (keyword) editor-id
  ; @param (map) body-props
  ;  {:suggestions-path (vector)}
  ;
  ; @return (map)
  ;  {:auto-title? (boolean)
  ;   :label-key (keyword)
  ;   :suggestion-keys (keywords in vector)}
  [_ {:keys [suggestions-path] :as body-props}]
  (merge {:auto-title? true
          :label-key   :name}
         (if suggestions-path {:suggestion-keys [:name]})
         (param body-props)))

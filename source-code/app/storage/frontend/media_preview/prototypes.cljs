
(ns app.storage.frontend.media-preview.prototypes
    (:require [mid-fruits.candy  :refer [param]]
              [mid-fruits.vector :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn preview-props-prototype
  ; @param (map) preview-props
  ;  {:items (namespaced maps)(opt)
  ;   :thumbnail (map)(opt)}
  ;
  ; @return (map)
  ;  {:sortable? (boolean)
  ;   :thumbnail (map)}
  [{:keys [items thumbnail] :as preview-props}]
  (merge {:max-count 8}
         (param preview-props)
         (if (vector/max? items 1)
             {:sortable? false})
         {:thumbnail (merge {:height    :5xl
                             :width     :5xl}
                            (param thumbnail))}))

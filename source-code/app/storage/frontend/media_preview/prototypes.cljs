
(ns app.storage.frontend.media-preview.prototypes
    (:require [candy.api  :refer [param]]
              [vector.api :as vector]))

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
  (let [items (vector/remove-items-by items nil?)]
       (merge {:max-count 8}
              (param preview-props)
              (if (vector/max? items 1)
                  {:sortable? false})
              {:items     items
               :thumbnail (merge {:height    :5xl
                                  :width     :5xl}
                                 (param thumbnail))})))

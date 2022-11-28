
(ns app.website-menus.frontend.picker.prototypes
    (:require [candy.api    :refer [param]]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn preview-props-prototype
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ; @param (namespaced map) client-link
  ;
  ; @return (map)
  ;  {:item-link (namespaced map)}
  [_ picker-props client-link]
  (merge (select-keys picker-props [:disabled? :placeholder])
         {:item-link client-link}))

(defn picker-props-prototype
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:multi-select? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:export-filter-f (function)
  ;   :import-id-f (function)
  ;   :on-select (metamorphic-event)
  ;   :toggle-label (metamorphic-content)
  ;   :transfer-id (keyword)}
  [_ {:keys [multi-select?] :as picker-props}]
  (merge {:toggle-label    (if multi-select? :select-website-menus! :select-website-menu!)}
         (param picker-props)
         {:export-filter-f (fn [menu-id] {:menu/id menu-id})
          :import-id-f     :menu/id
          :items-path      [:website-menus :picker/downloaded-items]
          :on-select       [:website-menus.selector/load-selector! :website-menus.selector picker-props]
          :transfer-id     :website-menus.lister}))

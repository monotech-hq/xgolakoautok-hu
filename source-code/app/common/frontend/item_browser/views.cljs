
(ns app.common.frontend.item-browser.views
    (:require [app.common.frontend.item-lister.views :as item-lister.views]
              [elements.api                          :as elements]
              [re-frame.api                          :as r]))

;; -- Search components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-browser-search-field
  ; @param (keyword) browser-id
  ; @param (map) field-props
  ;  {:disabled? (boolean)(opt)
  ;   :placeholder (metamorphic-content)(opt)
  ;   :search-keys (keywords in vector)}
  ;
  ; @usage
  ;  [item-browser-search-field :my-browser {...}]
  [browser-id field-props]
  [item-lister.views/item-lister-search-field browser-id field-props])

(defn item-browser-search-description
  ; @param (keyword) browser-id
  ; @param (map) description-props
  ;  {:disabled? (boolean)(opt)}
  ;
  ; @usage
  ;  [item-browser-search-description :my-browser {...}]
  [browser-id description-props]
  [item-lister.views/item-lister-search-description browser-id description-props])

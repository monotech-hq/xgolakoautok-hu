
(ns app.website-menus.frontend.preview.views
    (:require [app.common.frontend.api                       :as common]
              [app.website-menus.frontend.preview.prototypes :as preview.prototypes]
              [app.components.frontend.api                   :as components]
              [elements.api                                  :as elements]
              [random.api                                    :as random]
              [re-frame.api                                  :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-preview-data
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :item-link (namespaced map)
  ;    {:menu/id (string)}}
  [_ {{:menu/keys [id]} :item-link :keys [disabled?]}]
  (let []))

(defn- menu-preview-element
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  [preview-id preview-props]
  [:div {:style {:display "flex" :flex-wrap "wrap" :grid-gap "12px"}}
        ;[client-avatar       preview-id preview-props]
        [menu-preview-data preview-id preview-props]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-preview
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  [preview-id preview-props]
  [common/item-preview preview-id (assoc preview-props :preview-element #'menu-preview-element)])

(defn element
  ; @param (keyword)(opt) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :item-link (namespaced map)(opt)
  ;    {:menu/id (string)}
  ;   :label (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [menu-preview {...}]
  ;
  ; @usage
  ;  [menu-preview :my-menu-preview {...}]
  ([preview-props]
   [element (random/generate-keyword) preview-props])

  ([preview-id preview-props]
   (let [preview-props (preview.prototypes/preview-props-prototype preview-id preview-props)]
        [menu-preview preview-id preview-props])))

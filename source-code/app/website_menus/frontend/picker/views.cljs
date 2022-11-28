
(ns app.website-menus.frontend.picker.views
    (:require [app.common.frontend.api                      :as common]
              [app.components.frontend.api                  :as components]
              [app.website-menus.frontend.picker.prototypes :as picker.prototypes]
              [app.website-menus.frontend.preview.views     :as preview.views]
              [elements.api                                 :as elements]
              [random.api                                   :as random]
              [re-frame.api                                 :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-list-item
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:sortable? (boolean)(opt)}
  ; @param (integer) item-dex
  ; @param (namespaced map) menu-link
  ;  {:menu/id (string)}
  ; @param (map)(opt) drag-props
  ;  {:handle-attributes (map)
  ;   :item-attributes (map)}
  ([picker-id picker-props item-dex client-link]
   [menu-list-item picker-id picker-props item-dex client-link {}])

  ([picker-id {:keys [sortable?]} item-dex {:client/keys [id]} {:keys [handle-attributes item-attributes]}]
   (let [{:keys [modified-at name]} @(r/subscribe [:item-lister/get-item picker-id id])
         timestamp @(r/subscribe [:x.activities/get-actual-timestamp modified-at])]
        [components/item-list-row {:drag-attributes item-attributes
                                   :cells []
                                   :border (if (not= item-dex 0) :top)}])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-item
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ; @param (namespaced map) menu-link
  [picker-id picker-props menu-link]
  (let [preview-props (picker.prototypes/preview-props-prototype picker-id picker-props menu-link)]
       [preview.views/element picker-id preview-props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-list-header
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:sortable? (boolean)(opt)}
  [_ {:keys [sortable?]}]
  [components/item-list-header ::menu-list-header
                               {:cells []
                                :border :bottom}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-picker
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  [picker-id picker-props]
  [common/item-picker picker-id (assoc picker-props :item-element      #'menu-item
                                                    :list-item-element #'menu-list-item
                                                    :item-list-header  #'menu-list-header)])

(defn element
  ; @param (keyword)(opt) picker-id
  ; @param (map) picker-props
  ;  {:autosave? (boolean)(opt)
  ;    Default: false
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :max-count (integer)(opt)
  ;    Default: 8
  ;    W/ {:multi-select? true}
  ;   :multi-select? (boolean)(opt)
  ;    Default: false
  ;   :on-change (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja a kiválasztott elemet.
  ;   :on-save (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja a kiválasztott elemet.
  ;   :placeholder (metamorphic-content)(opt)
  ;   :required? (boolean)(opt)
  ;    Default: false
  ;   :sortable? (boolean)(opt)
  ;    Default: false
  ;    W/ {:multi-select? true}
  ;   :toggle-label (metamorphic-content)(opt)
  ;   :value-path (vector)}
  ;
  ; @usage
  ;  [menu-picker {...}]
  ;
  ; @usage
  ;  [menu-picker :my-menu-picker {...}]
  ([picker-props]
   [element (random/generate-keyword) picker-props])

  ([picker-id picker-props]
   (let [picker-props (picker.prototypes/picker-props-prototype picker-id picker-props)]
        [menu-picker picker-id picker-props])))

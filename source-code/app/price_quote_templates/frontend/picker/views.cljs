
(ns app.price-quote-templates.frontend.picker.views
    (:require [app.common.frontend.api                              :as common]
              [app.components.frontend.api                          :as components]
              [app.price-quote-templates.frontend.picker.prototypes :as picker.prototypes]
              [app.price-quote-templates.frontend.preview.views     :as preview.views]
              [elements.api                                         :as elements]
              [random.api                                           :as random]
              [re-frame.api                                         :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-list-item
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:sortable? (boolean)(opt)}
  ; @param (integer) item-dex
  ; @param (namespaced map) template-link
  ;  {:template/id (string)}
  ; @param (map)(opt) drag-props
  ;  {:handle-attributes (map)
  ;   :item-attributes (map)}
  ([picker-id picker-props item-dex template-link]
   [template-list-item picker-id picker-props item-dex template-link {}])

  ([picker-id picker-props item-dex template-link drag-props]))
   ; TODO

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-item
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ; @param (namespaced map) template-link
  [picker-id picker-props template-link]
  (let [preview-props (picker.prototypes/preview-props-prototype picker-id picker-props template-link)]
       [preview.views/element picker-id preview-props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-list-header
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:sortable? (boolean)(opt)}
  [_ {:keys [sortable?]}])
  ; TODO

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-picker
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  [picker-id picker-props]
  [common/item-picker picker-id (assoc picker-props :item-element      #'template-item
                                                    :list-item-element #'template-list-item
                                                    :item-list-header  #'template-list-header)])

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
  ;  [template-picker {...}]
  ;
  ; @usage
  ;  [template-picker :my-template-picker {...}]
  ([picker-props]
   [element (random/generate-keyword) picker-props])

  ([picker-id picker-props]
   (let [picker-props (picker.prototypes/picker-props-prototype picker-id picker-props)]
        [template-picker picker-id picker-props])))


(ns app.vehicle-types.frontend.picker.views
    (:require [app.vehicle-types.frontend.picker.prototypes :as picker.prototypes]
              [app.vehicle-types.frontend.preview.views     :as preview.views]
              [elements.api                                 :as elements]
              [random.api                                   :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-picker-previews
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  [picker-id picker-props]
  ; BUG#0889 (app.products.frontend.picker.views)
  (let [preview-props (picker.prototypes/preview-props-prototype picker-id picker-props)]
      ;[preview.views/element ::type-picker-previews preview-props]
       [preview.views/element picker-id preview-props]))

(defn- type-picker-button
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:disabled? (boolean)(opt)
  ;   :multi-select? (boolean)(opt)}
  [picker-id {:keys [disabled? multi-select?] :as picker-props}]
  (let [on-click [:vehicle-types.selector/load-selector! :vehicle-types.selector picker-props]]
       [:div {:style {:display :flex}}
             [elements/button {:color     :muted
                               :disabled? disabled?
                               :font-size :xs
                               :label     (if multi-select? :select-vehicle-types! :select-vehicle-type!)
                               :on-click  on-click}]]))

(defn- type-picker-label
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:disabled? (boolean)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :required? (boolean)(opt)}
  [_ {:keys [disabled? info-text label required?]}]
  (if label [elements/label {:content     label
                             :disabled?   disabled?
                             :info-text   info-text
                             :line-height :block
                             :required?   required?}]))

(defn- type-picker-body
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  [picker-id picker-props]
  [:<> [type-picker-label    picker-id picker-props]
       [type-picker-button   picker-id picker-props]
       [type-picker-previews picker-id picker-props]])

(defn- type-picker
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:indent (map)(opt)}
  [picker-id {:keys [indent] :as picker-props}]
  [elements/blank picker-id
                  {:content [type-picker-body picker-id picker-props]
                   :indent  indent}])

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
  ;   :model-id (string)
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
  ;   :value-path (vector)}
  ;
  ; @usage
  ;  [type-picker {...}]
  ;
  ; @usage
  ;  [type-picker :my-picker {...}]
  ([picker-props]
   [element (random/generate-keyword) picker-props])

  ([picker-id picker-props]
   (let [picker-props (picker.prototypes/picker-props-prototype picker-id picker-props)]
        [type-picker picker-id picker-props])))


(ns app.storage.frontend.media-picker.views
    (:require [app.storage.frontend.media-picker.prototypes :as media-picker.prototypes]
              [app.storage.frontend.media-preview.views     :as media-preview.views]
              [elements.api                                 :as elements]
              [mid-fruits.random                            :as random]
              [re-frame.api                                 :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn media-picker-previews
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  [picker-id picker-props]
  ; BUG#0889 (app.products.frontend.picker.views)
  (let [preview-props (media-picker.prototypes/preview-props-prototype picker-id picker-props)]
      ;[media-preview.views/element ::media-picker-previews preview-props]
       [media-preview.views/element picker-id preview-props]))

(defn media-picker-button
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:disabled? (boolean)(opt)
  ;   :multi-select? (boolean)(opt)
  ;   :toggle-label (metamorphic-content)(opt)}
  [picker-id {:keys [disabled? multi-select? toggle-label] :as picker-props}]
  (let [on-click [:storage.media-selector/load-selector! picker-id picker-props]]
       [:div {:style {:display :flex}}
             [elements/button {:color     :muted
                               :disabled? disabled?
                               :font-size :xs
                               :label     (or toggle-label (if multi-select? :select-items! :select-item!))
                               :on-click  on-click}]]))

(defn media-picker-label
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

(defn media-picker-body
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  [picker-id picker-props]
  [:<> [media-picker-label    picker-id picker-props]
       [media-picker-button   picker-id picker-props]
       [media-picker-previews picker-id picker-props]])

(defn- media-picker
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:indent (map)(opt)}
  [picker-id {:keys [indent] :as picker-props}]
  [elements/blank picker-id
                  {:content [media-picker-body picker-id picker-props]
                   :indent  indent}])

(defn element
  ; @param (keyword)(opt) picker-id
  ; @param (map) picker-props
  ;  {:autosave? (boolean)(opt)
  ;   Default: false
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :extensions (strings in vector)(opt)
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :max-count (integer)(opt)
  ;    Default: 8
  ;   :multi-select? (boolean)(opt)
  ;    Default: false
  ;   :on-save (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja a kiválasztott elemet.
  ;   :placeholder (metamorphic-content)(opt)
  ;   :required? (boolean)(opt)
  ;    Default: false
  ;   :sortable? (boolean)(opt)
  ;    Default: false
  ;   :thumbnail (map)(opt)
  ;    {:height (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;      Default: :5xl
  ;     :width (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;      Default: :5xl}
  ;   :toggle-label (metamorphic-content)(opt)
  ;   :value-path (vector)}
  ;
  ; @usage
  ;  [media-picker {...}]
  ;
  ; @usage
  ;  [media-picker :my-picker {...}]
  ([picker-props]
   [element (random/generate-keyword) picker-props])

  ([picker-id picker-props]
   (let [picker-props (media-picker.prototypes/picker-props-prototype picker-id picker-props)]
        [media-picker picker-id picker-props])))

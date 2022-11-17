
(ns app.products.frontend.picker.views
    (:require [app.products.frontend.picker.prototypes :as picker.prototypes]
              [app.products.frontend.preview.views     :as preview.views]
              [elements.api                            :as elements]
              [random.api                       :as random]
              [re-frame.api                            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-picker-previews
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  [picker-id picker-props]
  ; BUG#0889
  ; Ha egy felületen több product-picker komponens van megjelenítve és az általuk
  ; megjelenített product-preview komponensek megegyező azonosítóval rendelkeznek,
  ; akkor a product-preview komponensek által megjelenített dnd-kit/body komponensek
  ; is megegyező azonosítót kapnának, ami hibás működéshez vezethet!
  ; Pl.: A 0.1.7.8 verzióban a types.editor szerkesztő "Images" és "Files" felületén
  ;      is megjelenik egy-egy media-picker komponens (különböző azonosítókkal),
  ;      de a media-picker komponensek által megjelenített media-preview komponensek
  ;      megegyező azonosítókkal rendelkeznek (::media-picker-previews), ami miatt
  ;      ha az egyik felületen (pl. "Images") a dnd-kit/body komponens használatával
  ;      a felhasználó módosítja az elemek sorrendjét, majd a másik felületen (pl. "Files")
  ;      is módosítja az ott megjelenített elemek sorrendjét, akkor a megegyező azonosítóval
  ;      rendelkező dnd-kit/body komponensek a második módosításnál ("Files" felületen)
  ;      az első módosításkor ("Images" felületen) használt elem-azonosítókat használják
  ;      a drag-end-f függvény event paraméterének "over" kulcsához tartozó térképben!
  (let [preview-props (picker.prototypes/preview-props-prototype picker-id picker-props)]
      ;[preview.views/element ::product-picker-previews preview-props]
       [preview.views/element picker-id preview-props]))

(defn- product-picker-button
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:disabled? (boolean)(opt)
  ;   :multi-select? (boolean)(opt)}
  [picker-id {:keys [disabled? multi-select?] :as picker-props}]
  (let [on-click [:products.selector/load-selector! :products.selector picker-props]]
       [:div {:style {:display :flex}}
             [elements/button {:color     :muted
                               :disabled? disabled?
                               :font-size :xs
                               :label     (if multi-select? :select-products! :select-product!)
                               :on-click  on-click}]]))

(defn- product-picker-label
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

(defn- product-picker-body
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  [picker-id picker-props]
  [:<> [product-picker-label    picker-id picker-props]
       [product-picker-button   picker-id picker-props]
       [product-picker-previews picker-id picker-props]])

(defn- product-picker
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:indent (map)(opt)}
  [picker-id {:keys [indent] :as picker-props}]
  [elements/blank picker-id
                  {:content [product-picker-body picker-id picker-props]
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
  ;   :multi-select? (boolean)(opt)
  ;    Default: false
  ;   :on-change (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja a kiválasztott elemet.
  ;   :on-save (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja a kiválasztott elemet.
  ;   :required? (boolean)(opt)
  ;    Default: false
  ;   :placeholder (metamorphic-content)(opt)
  ;   :sortable? (boolean)(opt)
  ;    Default: false
  ;   :value-path (vector)}
  ;
  ; @usage
  ;  [product-picker {...}]
  ;
  ; @usage
  ;  [product-picker :my-picker {...}]
  ([picker-props]
   [element (random/generate-keyword) picker-props])

  ([picker-id picker-props]
   (let [picker-props (picker.prototypes/picker-props-prototype picker-id picker-props)]
        [product-picker picker-id picker-props])))

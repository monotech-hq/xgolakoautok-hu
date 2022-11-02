
(ns app.storage.frontend.media-preview.views
    (:require [app.storage.frontend.media-preview.prototypes :as media-preview.prototypes]
              [elements.api                                  :as elements]
              [io.api                                        :as io]
              [mid-fruits.candy                              :refer [return]]
              [mid-fruits.random                             :as random]
              [mid-fruits.vector                             :as vector]

              ; TEMP
              [plugins.dnd-kit.api :as dnd-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- media-preview-static-body
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :thumbnail (map)
  ; @param (string) media-link
  [preview-id {:keys [disabled?] {:keys [height width]} :thumbnail} {:media/keys [uri]}]
  [elements/thumbnail {:border-radius :s
                       :disabled?     disabled?
                       :height        height
                       :width         width
                       :uri           uri}])

(defn- media-preview-sortable-body
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (string) media-link
  ; @param (map) dnd-kit-props
  [preview-id preview-props item-dex media-link {:keys [attributes listeners]}]
  [:div (merge attributes listeners {:style {:cursor :grab}})
        [media-preview-static-body preview-id preview-props media-link]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- media-preview-static-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:items (namespaced maps in vector)}
  [preview-id {:keys [items] :as preview-props}]
  (letfn [(f [preview-list media-link] (conj preview-list [media-preview-static-body preview-id preview-props media-link]))]
         (reduce f [:div {:style {:display "flex" :flex-wrap "wrap" :grid-gap "24px"}}] items)))

(defn- media-preview-sortable-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:items (namespaced maps in vector)
  ;   :value-path (vector)}
  [preview-id {:keys [items value-path] :as preview-props}]
  [:div {:style {:display "flex" :flex-wrap "wrap" :grid-gap "24px"}}
        [dnd-kit/body preview-id
                      {:common-props     preview-props
                       :items            items
                       :item-id-f        :media/id
                       :item-element     #'media-preview-sortable-body
                       :on-order-changed [:db/set-item! value-path]}]])

(defn- media-preview-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:sortable? (boolean)(opt)}
  [preview-id {:keys [sortable?] :as preview-props}]
  (if sortable? [media-preview-sortable-list preview-id preview-props]
                [media-preview-static-list   preview-id preview-props]))

(defn- media-preview-label
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)}
  [_ {:keys [disabled? info-text label]}]
  (if label [elements/label {:content   label
                             :disabled? disabled?
                             :info-text info-text}]))

(defn- media-preview-placeholder-thumbnail
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :thumbnail (map)}
  [preview-id {:keys [disabled? thumbnail]}]
  [elements/thumbnail {:border-radius :s
                       :disabled?     disabled?
                       :height        (:height thumbnail)
                       :width         (:width  thumbnail)}])

(defn- media-preview-placeholder-label
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :placeholder (metamorphic-content)}
  [preview-id {:keys [disabled? placeholder]}]
  [elements/label {:color               :muted
                   :content             placeholder
                   :disabled?           disabled?
                   :font-size           :xs
                   :horizontal-position :left}])

(defn- media-preview-placeholder
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :placeholder (metamorphic-content)(opt)}
  [preview-id {:keys [disabled? placeholder] :as preview-props}]
  (cond (=     placeholder :empty-thumbnail) [media-preview-placeholder-thumbnail preview-id preview-props]
        (some? placeholder)                  [media-preview-placeholder-label     preview-id preview-props]))

(defn- media-preview
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:indent (map)(opt)
  ;   :items (namespaced maps in vector)(opt)}
  [preview-id {:keys [indent items] :as preview-props}]
  [elements/blank preview-id
                  {:content [:<> [media-preview-label preview-id preview-props]
                                 (if (vector/nonempty? items)
                                     [media-preview-list        preview-id preview-props]
                                     [media-preview-placeholder preview-id preview-props])]
                   :indent  indent}])

(defn element
  ; @param (keyword)(opt) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :items (namespaced maps in vector)(opt)
  ;    [{:id (string)
  ;      :uri (string)}
  ;     {...}]
  ;   :label (metamorphic-content)(opt)
  ;   :max-count (integer)(opt)
  ;    Default: 8
  ;   :placeholder (metamorphic-content)(opt)
  ;    :empty-thumbnail
  ;   :sortable? (boolean)(opt)
  ;    Default: false
  ;   :thumbnail (map)(opt)
  ;    {:height (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;      Default: :5xl
  ;     :width (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;      Default: :5xl}
  ;    :value-path (vector)(opt)
  ;     W/ {:sortable? true}}
  ;
  ; @usage
  ;  [media-preview {...}]
  ;
  ; @usage
  ;  [media-preview :my-preview {...}]
  ([preview-props]
   [element (random/generate-keyword) preview-props])

  ([preview-id preview-props]
   (let [preview-props (media-preview.prototypes/preview-props-prototype preview-props)]
        [media-preview preview-id preview-props])))

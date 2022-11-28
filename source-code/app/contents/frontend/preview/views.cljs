
(ns app.contents.frontend.preview.views
    (:require [app.common.frontend.api                  :as common]
              [app.components.frontend.api              :as components]
              [app.contents.frontend.handler.helpers    :as handler.helpers]
              [app.contents.frontend.preview.prototypes :as preview.prototypes]
              [elements.api                             :as elements]
              [random.api                               :as random]
              [re-frame.api                             :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-preview-data
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:color (keyword)
  ;   :disabled? (boolean)(opt)
  ;   :font-size (keyword)
  ;   :font-weight (keyword)
  ;   :item-link (namespaced map)
  ;    {:content/id (string)}
  ;   :max-lines (integer)(opt)
  ;   :style (map)(op)}
  [_ {{:content/keys [id]} :item-link :keys [color disabled? font-size font-weight max-lines style]}]
  (let [content-body @(r/subscribe [:x.db/get-item [:contents :preview/downloaded-items id :body]])]
       [elements/text {:color       color
                       :content     (handler.helpers/parse-content-body content-body)
                       :disabled?   disabled?
                       :font-size   font-size
                       :font-weight font-weight
                       :max-lines   max-lines
                       :style       style}]))

(defn- content-preview-element
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  [preview-id preview-props]
  [content-preview-data preview-id preview-props])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-preview
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  [preview-id preview-props]
  [common/item-preview preview-id (assoc preview-props :preview-element #'content-preview-element)])

(defn element
  ; @param (keyword)(opt) preview-id
  ; @param (map) preview-props
  ;  {:color (keyword)(opt)
  ;    :default, :highlight, :inherit, :muted
  ;    Default: :default
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :font-size (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :inherit
  ;    Default: :s
  ;   :font-weight (keyword)(opt)
  ;    :bold, :extra-bold, :inherit, :normal
  ;    Default: :normal
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :item-link (namespaced map)(opt)
  ;    {:content/id (string)}
  ;   :label (metamorphic-content)(opt)
  ;   :max-lines (integer)(opt)
  ;   :placeholder (metamorphic-content)(opt)
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [content-preview {...}]
  ;
  ; @usage
  ;  [content-preview :my-content-preview {...}]
  ([preview-props]
   [element (random/generate-keyword) preview-props])

  ([preview-id preview-props]
   (let [preview-props (preview.prototypes/preview-props-prototype preview-id preview-props)]
        [content-preview preview-id preview-props])))


(ns app.components.frontend.surface-box.views
    (:require [app.components.frontend.surface-box.prototypes :as surface-box.prototypes]
              [elements.api                                   :as elements]
              [random.api                                     :as random]
              [re-frame.api                                   :as r]
              [x.components.api                               :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- surface-box-query-placeholder
  ; @param (keyword) box-id
  [_]
  [elements/label {:color            :muted
                   :content          :downloading...
                   :font-size        :xs
                   :horizontal-align :center
                   :indent           {:bottom :s}
                   :line-height      :block}])

(defn- surface-box-query-content
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;  {:content (metamorphic-content)
  ;   :query (vector)
  ;   :refresh-interval (ms)(opt)}
  [box-id {:keys [content query refresh-interval]}]
  [x.components/querier box-id
                        {:content          content
                         :placeholder      #'surface-box-query-placeholder
                         :query            query
                         :refresh-interval refresh-interval}])

(defn- surface-box-static-content
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;  {:content (metamorphic-content)}
  [_ {:keys [content]}]
  [x.components/content content])

(defn- surface-box-content
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;  {:query (metamorphic-content)}
  [box-id {:keys [query] :as box-props}]
  (if query [surface-box-query-content  box-id box-props]
            [surface-box-static-content box-id box-props]))

(defn- surface-box-label
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;  {:disabled? (boolean)(opt)
  ;   :helper (metamorphic-content)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)}
  [_ {:keys [disabled? helper info-text label]}]
  (if label (let [viewport-large? @(r/subscribe [:x.environment/viewport-large?])]
                 [elements/label {:content     label
                                  :disabled?   disabled?
                                  :helper      helper
                                  :info-text   info-text
                                  :indent      {:top :xs :vertical :s}
                                  :font-size   (if viewport-large? :l :m)
                                  :line-height :block}])))

(defn- surface-box-body
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;  {:background-color (string)}
  [box-id {:keys [background-color content overflow] :as box-props}]
  (let [viewport-small? @(r/subscribe [:x.environment/viewport-small?])]
       [:div {:style {:background-color background-color
                      :border           "1px solid var( --border-color-highlight )"
                      :border-radius    (if viewport-small? "0" "var( --border-radius-m )")
                      :overflow         overflow}}
             [surface-box-label   box-id box-props]
             [surface-box-content box-id box-props]]))

(defn- surface-box
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :indent (map)(opt)
  ;   :style (map)(opt)}
  [box-id {:keys [class indent style] :as box-props}]
  [elements/blank box-id
                  {:class   class
                   :indent  indent
                   :content [surface-box-body box-id box-props]
                   :style   style}])

(defn component
  ; @param (keyword)(opt) box-id
  ; @param (map) box-props
  ;  {:background-color (string)(opt)
  ;    Default: "var( --fill-color )"
  ;   :class (keyword or keywords in vector)(opt)
  ;   :content (metamorphic-content)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :helper (metamorphic-content)(opt)
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :overflow (keyword)(opt)
  ;    :hidden, :visible
  ;    Default: :visible
  ;   :query (vector)(opt)
  ;   :refresh-interval (ms)(opt)
  ;    W/ {:query ...}
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [surface-box {...}]
  ;
  ; @usage
  ;  [surface-box :my-surface-box {...}]
  ([box-props]
   [component (random/generate-keyword) box-props])

  ([box-id box-props]
   (let [box-props (surface-box.prototypes/box-props-prototype box-props)]
        [surface-box box-id box-props])))

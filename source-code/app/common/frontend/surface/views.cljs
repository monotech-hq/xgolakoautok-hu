
(ns app.common.frontend.surface.views
    (:require [elements.api          :as elements]
              [layouts.surface-a.api :as surface-a]
              [re-frame.api          :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- go-back-button
  [_]
  [:div {:style {:display :flex :justify-content :center}}
        [elements/button ::go-back-button
                         {:border-radius :s
                          :hover-color   :highlight
                          :indent        {:top :m}
                          :label         :back!
                          :on-click      [:router/go-back!]}]])

;; -- Breadcrumbs components --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-breadcrumbs
  ; @param (keyword) surface-id
  ; @param (map) breadcrumbs-props
  ;  {:crumbs (maps in vector)
  ;   :disabled? (boolean)(opt)}
  ;
  ; @usage
  ;  [surface-breadcrumbs :my-surface {...}]
  [surface-id {:keys [crumbs disabled?]}]
  [elements/breadcrumbs ::surface-breadcrumbs
                        {:crumbs    crumbs
                         :disabled? disabled?
                         :indent    {:left :xs}}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-label
  ; @param (keyword) surface-id
  ; @param (map) description-props
  ;  {:disabled? (boolean)(opt)
  ;   :label (metamorphic-content)
  ;   :placeholder (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [surface-label :my-surface {...}]
  [surface-id {:keys [disabled? label placeholder]}]
  ; Ha nem egy közös elemben (pl. div) volt a sensor és a label, akkor bizonoyos
  ; esetekben (pl. horizontal-polarity elemben) nem megfelelő helyen érzékelt a sensor
  [:div [surface-a/title-sensor {:title label :offset -12}]
        (let [viewport-large? @(r/subscribe [:environment/viewport-large?])]
             [elements/label ::surface-label
                             {:content     label
                              :disabled?   disabled?
                              :font-size   (if viewport-large? :xxl :l)
                              :font-weight :extra-bold
                              :indent      {:left :xs}
                              :placeholder placeholder}])])

(defn surface-description
  ; @param (keyword) surface-id
  ; @param (map) description-props
  ;  {:description (metamorphic-content)
  ;   :disabled? (boolean)(opt)}
  ;
  ; @usage
  ;  [surface-description :my-surface {...}]
  [surface-id {:keys [description disabled?]}]
  [elements/label ::surface-description
                  {:color     :muted
                   :content   description
                   :disabled? disabled?
                   :font-size :xxs
                   :indent    {:left :xs}}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-box-layout-ghost-view
  ; @param (keyword) editor-id
  ; @param (map) view-props
  ;  {:breadcrumb-count (integer)}
  ;
  ; @usage
  ;  [surface-box-layout-ghost-view :my-editor {...}]
  [editor-id {:keys [breadcrumb-count]}]
  [:div {:style {:padding "0 12px" :width "100%"}}
        [:div {:style {:padding-bottom "6px" :width "240px"}}
              [elements/ghost {:height :xl}]]
        [:div {:style {:display "flex" :grid-column-gap "12px" :padding-top "6px"}}
              (letfn [(f [%1 %2] (conj %1 [:div {:style {:width "80px"}} [elements/ghost {:height :xs}]]))]
                     (reduce f [:<>] (range breadcrumb-count)))]
        [:div {:style {:display "flex" :flex-direction "column" :width "100%" :grid-row-gap "24px" :padding-top "96px"}}
              [:div {:style {:flex-grow 1}} [elements/ghost {:height :5xl :indent {}}]]
              [:div {:style {:flex-grow 1}} [elements/ghost {:height :5xl :indent {}}]]
              [:div {:style {:flex-grow 1}} [elements/ghost {:height :5xl :indent {}}]]]])


(ns app.views.frontend.error-screen.views
    (:require [app.common.frontend.api :as common]
              [elements.api            :as elements]
              [layouts.surface-a.api   :as surface-a]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- go-back-button
  ; @param (keyword) surface-id
  ; @param (map) screen-props
  [_ _]
  [:div {:style {:display :flex :justify-content :center}}
        [elements/button ::go-back-button
                         {:border-radius :s
                          :hover-color   :highlight
                          :indent        {:top :m}
                          :label         :back!
                          :on-click      [:x.router/go-back!]}]])

(defn- error-title
  ; @param (keyword) surface-id
  ; @param (map) screen-props
  ;  {:title (metamorphic-content)(opt)}
  [surface-id {:keys [title]}]
  (if title [elements/text ::error-title
                           {:content          title
                            :font-size        :xxl
                            :font-weight      :bold
                            :horizontal-align :center
                            :indent           {:top :xxl :vertical :s}
                            :line-height      :block
                            :selectable?      false}]))

(defn- error-helper
  ; @param (keyword) surface-id
  ; @param (map) screen-props
  ;  {:helper (metamorphic-content)(opt)}
  [surface-id {:keys [helper]}]
  (if helper [elements/text ::error-helper
                            {:content          helper
                             :font-size        :s
                             :horizontal-align :center
                             :indent           {:vertical :s}
                             :line-height      :block
                             :selectable?      false}]))

(defn- error-icon
  ; @param (keyword) surface-id
  ; @param (map) screen-props
  ;  {:icon (keyword)(opt)}
  [surface-id {:keys [icon]}]
  (if icon [elements/row ::error-icon-wrapper
                         {:content [elements/icon ::error-icon
                                                  {:icon icon
                                                   :size :xxl}]
                          :horizontal-align :center}]))

(defn- view-structure
  ; @param (keyword) surface-id
  ; @param (map) screen-props
  [surface-id screen-props]
  [:<> [error-icon     surface-id screen-props]
       [error-title    surface-id screen-props]
       [error-helper   surface-id screen-props]
       [go-back-button surface-id screen-props]])

(defn view
  ; @param (keyword) surface-id
  ; @param (map) screen-props
  ;  {:icon (keyword)(opt)
  ;   :helper (metamorphic-content)(opt)
  ;   :title (metamorphic-content)(opt)}
  [surface-id screen-props]
  [surface-a/layout surface-id
                    {:content [view-structure surface-id screen-props]}])


(ns app.views.frontend.error-screen.views
    (:require [app.common.frontend.api :as common]
              [elements.api            :as elements]
              [layouts.surface-a.api   :as surface-a]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- error-title
  [surface-id {:keys [title]}]
  [elements/text ::error-title
                 {:content          title
                  :font-size        :xxl
                  :font-weight      :bold
                  :horizontal-align :center
                  :indent           {:top :xxl :vertical :s}
                  :line-height      :block
                  :selectable?      false}])

(defn- error-helper
  [surface-id {:keys [helper]}]
  [elements/text ::error-helper
                 {:content          helper
                  :font-size        :s
                  :horizontal-align :center
                  :indent           {:vertical :s}
                  :line-height      :block
                  :selectable?      false}])

(defn- error-icon
  [surface-id {:keys [icon]}]
  (if icon [elements/row ::error-icon-wrapper
                         {:content [elements/icon ::error-icon
                                                  {:icon icon
                                                   :size :xxl}]
                          :horizontal-align :center}]))

(defn- view-structure
  [surface-id screen-props]
  [:<> [error-icon            surface-id screen-props]
       [error-title           surface-id screen-props]
       [error-helper          surface-id screen-props]
       [common/go-back-button surface-id]])

(defn view
  [surface-id screen-props]
  [surface-a/layout surface-id
                    {:content [view-structure surface-id screen-props]}])

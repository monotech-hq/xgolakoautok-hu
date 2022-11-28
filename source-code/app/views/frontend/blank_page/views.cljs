
(ns app.views.frontend.blank-page.views
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [elements.api                :as elements]
              [layouts.surface-a.api       :as surface-a]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content
  [_ {:keys [helper]}]
  [elements/text ::content
                 {:color            :highlight
                  :content          helper
                  :font-size        :xs
                  :font-weight      :bold
                  :horizontal-align :center
                  :indent           {:top :xxl :vertical :xs}
                  :line-height      :block}])


(defn- breadcrumbs
  [surface-id {:keys [title]}]
  [elements/breadcrumbs ::breadcrumbs
                        {:crumbs [{:label :app-home
                                   :route "/@app-home"}
                                  {:label title}]
                         :indent {:left :xs}}])

(defn- title
  [surface-id {:keys [title]}]
  [components/surface-label surface-id
                            {:label title}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-structure
  [surface-id page-props]
  (if-let [page-loaded? @(r/subscribe [:x.db/get-item [:views :blank-page/page-loaded?]])]
          [:<> [title       surface-id page-props]
               [breadcrumbs surface-id page-props]
               [content     surface-id page-props]]
          [components/ghost-view {:breadcrumb-count 2 :layout :box-surface}]))

(defn view
  [surface-id page-props]
  [surface-a/layout surface-id
                    {:content [view-structure surface-id page-props]}])

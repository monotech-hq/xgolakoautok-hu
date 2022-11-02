
(ns app.views.frontend.blank-page.views
    (:require [app.common.frontend.api :as common]
              [elements.api            :as elements]
              [layouts.surface-a.api   :as surface-a]
              [re-frame.api            :as r]))

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
                  :indent           {:top :xxl :vertical :xs}}])


(defn- breadcrumbs
  [surface-id {:keys [title]}]
  [elements/breadcrumbs ::breadcrumbs
                        {:crumbs [{:label :app-home
                                   :route "/@app-home"}
                                  {:label title}]
                         :indent {:left :xs}}])

(defn- title
  [surface-id {:keys [title]}]
  [common/surface-label surface-id
                        {:label title}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-structure
  [surface-id page-props]
  (if-let [page-loaded? @(r/subscribe [:db/get-item [:views :blank-page/page-loaded?]])]
          [:<> [title       surface-id page-props]
               [breadcrumbs surface-id page-props]
               [content     surface-id page-props]]
          [common/surface-box-layout-ghost-view :views.blank-page/view {:breadcrumb-count 2}]))

(defn view
  [surface-id page-props]
  [surface-a/layout surface-id
                    {:content [view-structure surface-id page-props]}])

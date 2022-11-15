
(ns app.pages.frontend.lister.views
    (:require [app.common.frontend.api :as common]
              [elements.api            :as elements]
              [engines.item-lister.api :as item-lister]
              [layouts.surface-a.api   :as surface-a]
              [re-frame.api            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? :pages.lister])]
          [common/item-lister-download-info :pages.lister {}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- page-item-structure
  [lister-id item-dex {:keys [body modified-at name]}]
  (let [timestamp @(r/subscribe [:activities/get-actual-timestamp modified-at])]
       [common/list-item-structure lister-id item-dex
                                   {:cells [[common/list-item-thumbnail-icon lister-id item-dex {:icon :article :icon-family :material-icons-outlined}]
                                            [common/list-item-primary-cell   lister-id item-dex {:label name :stretch? true :placeholder :unnamed-page :description body}]
                                            [common/list-item-detail         lister-id item-dex {:page timestamp :width "160px"}]
                                            [common/list-item-marker         lister-id item-dex {:icon :navigate_next}]]}]))

(defn- page-item
  [lister-id item-dex {:keys [id] :as page-item}]
  [elements/toggle {:page        [page-item-structure lister-id item-dex page-item]
                    :hover-color :highlight
                    :on-click    [:x.router/go-to! (str "/@app-home/pages/"id)]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- page-list
  [lister-id items]
  [common/item-list lister-id {:item-element #'page-item :items items}])

(defn- page-lister-body
  []
  [item-lister/body :pages.lister
                    {:default-order-by :modified-at/descending
                     :items-path       [:pages :lister/downloaded-items]
                     :error-element    [common/error-content {:error :the-content-you-opened-may-be-broken}]
                     :ghost-element    #'common/item-lister-ghost-element
                     :list-element     #'page-list}])

(defn- page-lister-header
  []
  [common/item-lister-header :pages.lister
                             {:cells [[common/item-lister-header-spacer :pages.lister {:width "108px"}]
                                      [common/item-lister-header-cell   :pages.lister {:label :name          :order-by-key :name :stretch? true}]
                                      [common/item-lister-header-cell   :pages.lister {:label :last-modified :order-by-key :modified-at :width "160px"}]
                                      [common/item-lister-header-spacer :pages.lister {:width "36px"}]]}])

(defn- body
  []
  [common/item-lister-wrapper :pages.lister
                              {:body   #'page-lister-body
                               :header #'page-lister-header}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn create-item-button
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :pages.lister])
        create-page-uri (str "/@app-home/pages/create")]
       [common/item-lister-create-item-button :pages.lister
                                              {:disabled?       lister-disabled?
                                               :create-item-uri create-page-uri}]))

(defn- search-field
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :pages.lister])]
       [common/item-lister-search-field :pages.lister
                                        {:disabled?   lister-disabled?
                                         :placeholder :search-in-pages
                                         :search-keys [:name]}]))

(defn- search-description
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :pages.lister])]
       [common/item-lister-search-description :pages.lister
                                              {:disabled? lister-disabled?}]))

(defn- breadcrumbs
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :pages.lister])]
       [common/surface-breadcrumbs :pages.lister/view
                                   {:crumbs [{:label :app-home :route "/@app-home"}
                                             {:label :pages}]
                                    :disabled? lister-disabled?}]))

(defn- label
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :pages.lister])]
       [common/surface-label :pages.lister/view
                             {:disabled? lister-disabled?
                              :label     :pages}]))

(defn- header
  []
  (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? :pages.lister])]
          [:<> [:div {:style {:display "flex" :justify-content "space-between" :flex-wrap "wrap" :grid-row-gap "24px"}}
                     [:div [label]
                           [breadcrumbs]]
                     [:div [create-item-button]]]
               [search-field]
               [search-description]]
          [common/item-lister-ghost-header :pages.lister {}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-structure
  []
  [:<> [header]
       [body]
       [footer]])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'view-structure}])

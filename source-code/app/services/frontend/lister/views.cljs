
(ns app.services.frontend.lister.views
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [elements.api                :as elements]
              [engines.item-lister.api     :as item-lister]
              [layouts.surface-a.api       :as surface-a]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? :services.lister])]
          [common/item-lister-download-info :services.lister {}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- service-item-structure
  [lister-id item-dex {:keys [item-number modified-at name]}]
  (let [timestamp  @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
        item-last? @(r/subscribe [:item-lister/item-last? lister-id item-dex])]
       [common/list-item-structure {:cells [[ {:icon :workspace_premium}]
                                            [common/list-item-label     {:content name :stretch? true :placeholder :unnamed-service}]
                                            [common/list-item-detail    {:content item-number :width "160px"}]
                                            [common/list-item-detail    {:content timestamp :width "160px"}]
                                            [components/list-item-marker    {:icon :navigate_next}]]
                                    :separator (if-not item-last? :bottom)}]))

(defn- service-item
  [lister-id item-dex {:keys [id] :as service-item}]
  [elements/toggle {:content     [service-item-structure lister-id item-dex service-item]
                    :hover-color :highlight
                    :on-click    [:x.router/go-to! (str "/@app-home/services/"id)]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- service-list
  []
  (let [items @(r/subscribe [:item-lister/get-downloaded-items :services.lister])]
       [common/item-list :services.lister {:item-element #'service-item :items items}]))

(defn- service-lister-body
  []
  [item-lister/body :services.lister
                    {:default-order-by :modified-at/descending
                     :items-path       [:services :lister/downloaded-items]
                     :error-element    [components/error-content {:error :the-content-you-opened-may-be-broken}]
                     :ghost-element    [common/item-lister-ghost-element]
                     :list-element     [service-list]}])

(defn- service-lister-header
  []
  [common/item-lister-header :services.lister
                             {:cells [[common/item-lister-header-spacer :services.lister {:width "108px"}]
                                      [common/item-lister-header-cell   :services.lister {:label :name          :order-by-key :name :stretch? true}]
                                      [common/item-lister-header-cell   :products.lister {:label :item-number   :order-by-key :item-number :width "160px"}]
                                      [common/item-lister-header-cell   :services.lister {:label :last-modified :order-by-key :modified-at :width "160px"}]
                                      [common/item-lister-header-spacer :services.lister {:width "36px"}]]}])

(defn- body
  []
  [common/item-lister-wrapper :services.lister
                              {:body   #'service-lister-body
                               :header #'service-lister-header}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn create-item-button
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :services.lister])
        create-service-uri (str "/@app-home/services/create")]
       [common/item-lister-create-item-button :services.lister
                                              {:disabled?       lister-disabled?
                                               :create-item-uri create-service-uri}]))

(defn- search-field
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :services.lister])]
       [common/item-lister-search-field :services.lister
                                        {:disabled?   lister-disabled?
                                         :placeholder :search-in-services
                                         :search-keys [:item-number :name]}]))

(defn- search-description
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :services.lister])]
       [common/item-lister-search-description :services.lister
                                              {:disabled? lister-disabled?}]))

(defn- breadcrumbs
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :services.lister])]
       [components/surface-breadcrumbs ::breadcrumbs
                                       {:crumbs [{:label :app-home :route "/@app-home"}
                                                 {:label :services}]
                                        :disabled? lister-disabled?}]))

(defn- label
  []
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? :services.lister])]
       [components/surface-label ::label
                                 {:disabled? lister-disabled?
                                  :label     :services}]))

(defn- header
  []
  (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? :services.lister])]
          [:<> [:div {:style {:display "flex" :justify-content "space-between" :flex-wrap "wrap" :grid-row-gap "24px"}}
                     [:div [label]
                           [breadcrumbs]]
                     [:div [create-item-button]]]
               [search-field]
               [search-description]]
          [common/item-lister-ghost-header :services.lister {}]))

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

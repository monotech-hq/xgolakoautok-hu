
(ns app.frame.frontend.views
    (:require [app.common.frontend.api  :as common]
              [app.frame.frontend.state :as state]
              [elements.api             :as elements]
              [mid-fruits.css           :as css]
              [re-frame.api             :as r]
              [x.app-components.api     :as x.components]))

;; -- Footer components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [:div {:style {:background-color "#eee9ff"}}
        "footer"])

;; -- Ad components -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- main-ad-small
  []
  [:div {:style {:display "flex" :justify-content "center" :padding "72px 36px 36px 36px"}}
        [:div {:style {:background-color "#cebeff"
                       :height "250px" :width "248px"}}]])

(defn- main-ad-medium
  []
  [:div {:style {:display "flex" :justify-content "center" :padding "72px 36px 36px 36px"}}
        [:div {:style {:background-color "#cebeff"
                       :height "250px" :width "408px"}}]])

(defn- main-ad-large
  []
  [:div {:style {:display "flex" :justify-content "center" :padding "48px 36px 36px 36px"}}
        [:div {:style {:background-color "#cebeff"
                       :height "250px" :width "970px"}}]])

(defn- ad
  []
  (if-let [viewport-large? @(r/subscribe [:environment/viewport-large?])]
          [main-ad-large]
          (if-let [viewport-medium? @(r/subscribe [:environment/viewport-medium?])]
                  [main-ad-medium]
                  [main-ad-small])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- tool-group-item
  [{:keys [label route]}]
  [elements/button {:label    label
                    :on-click [:router/go-to! route]}])

(defn- tool-group-items
  [group-items]
  (letfn [(f [group-items group-item]
             (conj group-items [tool-group-item group-item]))]
         (reduce f [:<>] group-items)))

(defn- tool-group-label
  [group-name]
  [elements/label {:content   group-name
                   :color     "#333"
                   :font-size :xs}])

(defn- tool-group
  [[group-name group-items]]
  [:div {:style {:padding "12px"}}
        [tool-group-label group-name]
        [tool-group-items group-items]])

(defn- tool-list
  []
  (let [tool-items @(r/subscribe [:db/get-item [:frame :tool-handler/data-items]])]
       [:<> (map (fn [%] ^{:key (random-uuid)} [tool-group %])
                 (group-by :group tool-items))]))

(defn- tools
  []
  [:div {:style {:background-color "#eee9ff" :display "flex" :padding-top "48px" :width "100%"}}
        [tool-list]])

;; -- Main-bar components -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- home-icon-button
  []
  [elements/icon-button ::home-icon-button
                        {:icon        :lightbulb
                         :icon-family :material-icons-outlined
                         :on-click    [:router/go-to! "/@app-home"]}])

(defn- tools-icon-button
  []
  [elements/icon-button ::tools-icon-button
                        {:icon          :more_horiz
                         :on-mouse-over {:fx [:frame/show-menu! :tools]}}])

(defn- share-icon-button
  []
  [elements/icon-button ::user-icon-button
                        {:icon          :share
                         :icon-family   :material-icons-outlined
                         :on-mouse-over {:fx [:frame/show-menu! :share]}}])

(defn- user-icon-button
  []
  [elements/icon-button ::user-icon-button
                        {:icon        :account_circle
                         :icon-family :material-icons-outlined
                         :on-click    []}])

(defn- home-button
  []
  [elements/button ::home-button
                   {:font-size   :xs
                    :indent      {:horizontal :xxs :left :xs}
                    :icon        :lightbulb
                    :icon-family :material-icons-outlined
                    :label       "Monotools.com"
                    :on-click    [:router/go-to! "/@app-home"]}])

(defn- share-button
  []
  [elements/button ::share-button
                   {:font-size     :xs
                    :icon          (case @state/VISIBLE-MENU :share :expand_more :placeholder)
                    :indent        {:horizontal :xxs :left :xxl}
                    :on-mouse-over {:fx [:frame/show-menu! :share]}
                    :label         :share!}])

(defn- tools-button
  []
  [elements/button ::tools-button
                   {:font-size     :xs
                    :icon          (case @state/VISIBLE-MENU :tools :expand_more :placeholder)
                    :indent        {:horizontal :xxs :left :xxl}
                    :on-mouse-over {:fx [:frame/show-menu! :tools]}
                    :label         :tools}])

(defn- logout-button
  []
  [elements/button ::logout-button
                   {:font-size :xs
                    :indent    {:horizontal :xxs :right :s}
                    :label     :logout!
                    :on-click  [:user/logout!]}])

(defn- login-button
  []
  [elements/button ::login-button
                   {:font-size :xs
                    :indent    {:horizontal :xxs :right :s}
                    :label     :login!
                    :on-click  [:router/go-to! "/login"]}])

(defn- signup-button
  []
  [elements/button ::signup-button
                   {:font-size :xs
                    :indent    {:horizontal :xxs :right :s}
                    :label     :sign-up!}])

(defn- main-bar-small
  []
  [elements/horizontal-polarity {:end-content   [:<> [share-icon-button]
                                                     [user-icon-button]]
                                 :start-content [:<> [home-icon-button]
                                                     [tools-icon-button]]}])

(defn- main-bar-large
  []
  (let [user-identified? @(r/subscribe [:user/user-identified?])]
       [elements/horizontal-polarity {:end-content   [:<> [signup-button]
                                                          (if user-identified? [logout-button]
                                                                               [login-button])]
                                      :start-content [:<> [home-button]
                                                          [tools-button]
                                                          [share-button]]}]))

;; -- Tools menu --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- tools-menu-small
  []
  [:div "Tools"])

(defn- tools-menu-large
  []
  [:div "Tools"])

(defn- tools-menu
  []
  (if-let [viewport-large? @(r/subscribe [:environment/viewport-large?])]
          [tools-menu-large]
          [tools-menu-small]))

;; -- Share-menu components ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- share-menu-small
  []
  [:div "Share"])

(defn- share-menu-large
  []
  [:div "Share"])

(defn- share-menu
  []
  (if-let [viewport-large? @(r/subscribe [:environment/viewport-large?])]
          [share-menu-large]
          [share-menu-small]))

;; -- Menu components----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-small
  []
  [:div {:style {:background-color "#eee9ff"}}
        (case @state/VISIBLE-MENU :share [share-menu]
                                  :tools [tools-menu]
                                         [:<>])])

(defn- menu-large
  []
  [:div {:style {:background-color "#eee9ff"}}
        (case @state/VISIBLE-MENU :share [share-menu]
                                  :tools [tools-menu]
                                         [:<>])])

;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header-small
  []
  [:<> [main-bar-small]
       [menu-small]])

(defn- header-large
  []
  [:<> [main-bar-large]
       [menu-large]])

(defn- header
  []
  [:div {:style {:background-color "#eee9ff" :left "0" :position "fixed" :top "0" :width "100%"}
         :on-mouse-out #(r/dispatch-fx [:frame/hide-menu!])}
        (if-let [viewport-large? @(r/subscribe [:environment/viewport-large?])]
                [header-large]
                [header-small])])

;; -- Wrapper components ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn wrapper
  [{:keys [content]}]
  [:<> [:div {:style {:background-color "#fafafa" :display "flex" :flex-direction "column" :min-height "100vh" :padding-top "48px"}}
             [:div {:style {:display "flex" :flex-direction "column" :flex-grow "1" :align-items "center"}}
                   [ad]
                   [:div {:style {:max-width "970px" :width "100%"}}
                         [x.components/content content]]]
             [x.components/content footer]]
       [:div {:style {:background-color "#eee9ff"}}
             [common/credits]]
       [header]])


(ns app.user.frontend.login-screen.views
    (:require [elements.api :as elements]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- app-title-label
  []
  (let [synchronizing? @(r/subscribe [:sync/listening-to-request? :user/authenticate!])
        app-title      @(r/subscribe [:core/get-app-config-item :app-title])]
       [elements/label ::app-title-label
                       {:content          app-title
                        :disabled?        synchronizing?
                        :font-weight      :extra-bold
                        :horizontal-align :center
                        :indent           {:horizontal :xs}}]))

;; -- Login form components ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- login-error-message
  []
  [elements/label ::login-error-message
                  {:content          :incorrect-email-address-or-password
                   :color            :warning
                   :horizontal-align :center
                   :indent           {:horizontal :xs}}])

(defn- email-address-field
  []
  (let [synchronizing? @(r/subscribe [:sync/listening-to-request? :user/authenticate!])]
       [elements/text-field ::email-address-field
                            {:autofill-name :email-address
                             :disabled?     synchronizing?
                             :indent        {:top :xs :vertical :xs}
                             :label         :email-address
                             :required?     :unmarked
                             :value-path    [:user :login-screen/login-data :email-address]}]))

(defn- password-field
  []
  (let [synchronizing? @(r/subscribe [:sync/listening-to-request? :user/authenticate!])]
       [elements/password-field ::password-field
                                {:autofill-name :password
                                 :disabled?     synchronizing?
                                 :indent        {:top :xs :vertical :xs}
                                 :required?     :unmarked
                                 :value-path    [:user :login-screen/login-data :password]}]))

(defn- forgot-password-button
  []
  (let [synchronizing? @(r/subscribe [:sync/listening-to-request? :user/authenticate!])]
       [:div {:style {:display :flex :justify-content :flex-end}}
             [elements/button ::forgot-password-button
                              {:color     :highlight
                               ;:disabled? synchronizing?
                               ; TEMP
                               :disabled? true
                               :font-size :xs
                               :indent    {:vertical :s}
                               :label     :forgot-password
                               :on-click  [:router/go-to! "/@app-home/forgot-password"]}]]))

(defn- login-button
  []
  (let [disabled? @(r/subscribe [:user.login-screen/login-button-disabled?])]
       [elements/button ::login-button
                        {:background-color :primary
                         :hover-color      :primary
                         :disabled?        disabled?
                         :label            :login!
                         :keypress         {:key-code 13 :required? true}
                         :indent           {:bottom :xs :top :l :vertical :xs}
                         :on-click         [:user.login-screen/login!]}]))

(defn- signup-button
  []
  (let [synchronizing? @(r/subscribe [:sync/listening-to-request? :user/authenticate!])]
       [elements/button ::signup-button
                        {:background-color :highlight
                         :border-radius    :s
                         ;:disabled?        synchronizing?
                         ; TEMP
                         :color :muted
                         :disabled? true
                         :hover-color      :highlight
                         :indent           {:bottom :xs :vertical :xs}
                         :label            :create-account!
                         :on-click         [:router/go-to! "/@app-home/create-account"]}]))

(defn- login-form
  []
  (let [login-attempted? @(r/subscribe [:user/login-attempted?])]
       [:<> [app-title-label]
            (if login-attempted? [login-error-message])
            [email-address-field]
            [password-field]
            [forgot-password-button]
            [login-button]
            [signup-button]]))

;; -- Logout form components --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- logout-button
  []
  [elements/button ::logout-button
                   {:hover-color :highlight
                    :indent      {:bottom :xs :vertical :xs}
                    :label       :logout!
                    :on-click    [:user/logout!]}])

(defn- continue-as-button
  []
  (let [user-name @(r/subscribe [:user/get-user-name])]
       [elements/button ::continue-as-button
                        {:color       :primary
                         :keypress    {:key-code 13}
                         :hover-color :highlight
                         :indent      {:vertical :xs}
                         :label       {:content :continue-as! :suffix user-name}
                         :on-click    [:router/go-home!]}]))

(defn- user-name-label
  []
  (let [user-first-name @(r/subscribe [:user/get-user-first-name])
        user-last-name  @(r/subscribe [:user/get-user-first-name])
        user-full-name  @(r/subscribe [:locales/get-ordered-name user-first-name user-last-name])]
       [elements/label ::user-name-label
                       {:content          {:content :signed-in-as :suffix user-full-name}
                        :horizontal-align :center
                        :indent           {:top :s :vertical :xs}}]))

(defn- user-email-address-label
  []
  (let [user-email-address @(r/subscribe [:user/get-user-email-address])]
       [elements/label ::user-email-address-label
                       {:color            :muted
                        :content          user-email-address
                        :font-size        :xs
                        :horizontal-align :center
                        :indent           {:bottom :m :vertical :xs}}]))

(defn- logged-in-form
  []
  [:<> [app-title-label]
       [user-name-label]
       [user-email-address-label]
       [continue-as-button]
       [logout-button]])

;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  []
  (let [viewport-small? @(r/subscribe [:environment/viewport-small?])]
       [:div#login-screen--body {:style (if viewport-small? {:background-color "var( --fill-color )"
                                                             :width            "320px"}
                                                            {:background-color "var( --fill-color )"
                                                             :border-color     "var( --border-color-highlight )"
                                                             :border-radius    "var( --border-radius-m )"
                                                             :border-style     "solid"
                                                             :border-width     "1px"
                                                             :width            "320px"})}
                                (if-let [user-identified? @(r/subscribe [:user/user-identified?])]
                                        [logged-in-form]
                                        [login-form])]))

(defn- login-screen
  []
  [:div#login-screen {:style {:align-items     "center"
                              :display         "flex"
                              :justify-content "center"
                              :min-height      "100vh"}}
                     [body]])

(defn view
  [surface-id]
  [login-screen])

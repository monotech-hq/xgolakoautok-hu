
(ns app.user.frontend.login-screen.views
    (:require [elements.api :as elements]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- app-title-label
  []
  (let [synchronizing? @(r/subscribe [:x.sync/listening-to-request? :x.user/authenticate!])
        app-title      @(r/subscribe [:x.core/get-app-config-item :app-title])]
       [elements/label ::app-title-label
                       {:content          app-title
                        :disabled?        synchronizing?
                        :font-weight      :extra-bold
                        :horizontal-align :center
                        :indent           {:horizontal :xs}
                        :line-height      :block}]))

;; -- Login form components ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- login-failured-message
  []
  (let [login-failure @(r/subscribe [:x.user/get-login-failure])
        error-message (case (:status login-failure) 403 :permission-denied
                                                    401 :incorrect-email-address-or-password
                                                        :unkown-error)]
       [elements/label ::login-failured-message
                       {:content          error-message
                        :color            :warning
                        :horizontal-align :center
                        :indent           {:horizontal :xs}
                        :line-height      :block}]))

(defn- email-address-field
  []
  (let [synchronizing? @(r/subscribe [:x.sync/listening-to-request? :x.user/authenticate!])]
       [elements/text-field ::email-address-field
                            {:autofill-name :email-address
                             :disabled?     synchronizing?
                             :indent        {:top :xs :vertical :xs}
                             :label         :email-address
                             :required?     :unmarked
                             :value-path    [:user :login-screen/login-data :email-address]}]))

(defn- password-field
  []
  (let [synchronizing? @(r/subscribe [:x.sync/listening-to-request? :x.user/authenticate!])]
       [elements/password-field ::password-field
                                {:autofill-name :password
                                 :disabled?     synchronizing?
                                 :indent        {:top :xs :vertical :xs}
                                 :required?     :unmarked
                                 :value-path    [:user :login-screen/login-data :password]}]))

(defn- forgot-password-button
  []
  (let [synchronizing? @(r/subscribe [:x.sync/listening-to-request? :x.user/authenticate!])]
       [:div {:style {:display :flex :justify-content :flex-end}}
             [elements/button ::forgot-password-button
                              {:color     :highlight
                               ;:disabled? synchronizing?
                               ; TEMP
                               :disabled? true
                               :font-size :xs
                               :indent    {:vertical :s}
                               :label     :forgot-password
                               :on-click  [:x.router/go-to! "/@app-home/forgot-password"]}]]))

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
  (let [synchronizing? @(r/subscribe [:x.sync/listening-to-request? :x.user/authenticate!])]
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
                         :on-click         [:x.router/go-to! "/@app-home/create-account"]}]))

(defn- login-effect
  []
  [:<> [:style {:type "text/css"}
               "#mt-login-effect {left: 0; top: 0; position: absolute; height: 100%; width: 100%;"
               "                  background-color: var( --fill-color ); border-radius: var( --border-radius-m );"
               "                  align-items: center; display: flex; justify-content: center; }"

               "#mt-login-effect--icon::before {border-radius: 50%; position: absolute; top: 0; left: 0; width: 48px; height: 48px;"
               "                                animation-iteration-count: infinite; animation-duration: 1.5s;"
               "                                content: ''; animation-name: mt-login-effect; }"

               "@keyframes mt-login-effect {  0% {opacity: 1; transform: scale(1); border:  2px solid #ddd; }"
               "                             70% {opacity: 0; transform: scale(3); border: 10px solid #ddd; }"
               "                            100% {opacity: 0; transform: scale(1); border:  2px solid #ddd; }}"]
       [:div {:id :mt-login-effect}
             [:div {:id :mt-login-effect--icon}
                   [elements/icon {:color       "#7bb"
                                   :icon        :badge
                                   :icon-family :material-icons-outlined
                                   :size        :l}]]]])

(defn- login-form
  []
  [:<> [app-title-label]
       (if-let [login-failured? @(r/subscribe [:x.user/login-failured?])]
               [login-failured-message])
       [email-address-field]
       [password-field]
       [forgot-password-button]
       [login-button]
       [signup-button]])
      ;(if-let [effect-visible? @(r/subscribe [:x.db/get-item [:user :login-screen/meta-items :effect-visible?]]
      ;        [login-effect])

;; -- Logout form components --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- logout-button
  []
  [elements/button ::logout-button
                   {:hover-color :highlight
                    :indent      {:bottom :xs :vertical :xs}
                    :label       :logout!
                    :on-click    [:x.user/logout!]}])

(defn- continue-as-button
  []
  (let [user-name @(r/subscribe [:x.user/get-user-name])]
       [elements/button ::continue-as-button
                        {:color       :primary
                         :keypress    {:key-code 13}
                         :hover-color :highlight
                         :indent      {:vertical :xs}
                         :label       {:content :continue-as! :suffix user-name}
                         :on-click    [:x.router/go-home!]}]))

(defn- user-name-label
  []
  (let [user-first-name @(r/subscribe [:x.user/get-user-first-name])
        user-last-name  @(r/subscribe [:x.user/get-user-first-name])
        user-full-name  @(r/subscribe [:x.locales/get-ordered-name user-first-name user-last-name])]
       [elements/label ::user-name-label
                       {:content          {:content :signed-in-as :suffix user-full-name}
                        :horizontal-align :center
                        :indent           {:top :s :vertical :xs}
                        :line-height      :block}]))

(defn- user-email-address-label
  []
  (let [user-email-address @(r/subscribe [:x.user/get-user-email-address])]
       [elements/label ::user-email-address-label
                       {:color            :muted
                        :content          user-email-address
                        :font-size        :xs
                        :horizontal-align :center
                        :indent           {:bottom :m :vertical :xs}
                        :line-height      :block}]))

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
  (let [viewport-small? @(r/subscribe [:x.environment/viewport-small?])]
       [:div#login-screen--body {:style (if viewport-small? {:background-color "var( --fill-color )"
                                                             :width            "320px"}
                                                            {:background-color "var( --fill-color )"
                                                             :border-color     "var( --border-color-highlight )"
                                                             :border-radius    "var( --border-radius-m )"
                                                             :border-style     "solid"
                                                             :border-width     "1px"
                                                             :width            "320px"})}
                                (if-let [user-identified? @(r/subscribe [:x.user/user-identified?])]
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

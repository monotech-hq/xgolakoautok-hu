
(ns site.xgo.pages.main-page.backend.lifecycles
    (:require [x.core.api :as x.core]))

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot {:dispatch-n [;[:x.environment/add-css! {:uri "/site/css/site.css"}]
                                 [:x.router/add-route! :main-page/route
                                                     {:client-event   [:main-page/load-page!]
                                                      :js-build       :site
                                                      :route-template "/"}]
                                 [:x.router/add-route! :main-page/route
                                                     {:client-event   [:main-page/load-page!]
                                                      :js-build       :site
                                                      :route-template "/:category"}]]}})

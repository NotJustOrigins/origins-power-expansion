def define_env(env):
    env.variables['origins_docs'] = "https://origins.readthedocs.io/en/0.7.3/"

    @env.macro
    def get_data_type(data_type):
        link = "{}{}/".format(env.variables.origins_docs, data_type)
        return "[{}]({})".format(data_type.capitalize(), link)

    def build_field_row(field, type, default, description):
        return "`{}`|{}|{}|{}".format(field.lower(), get_data_type(type), default, description)

    @env.macro
    def build_field_table(*rows):
        split_rows = [x.split("|") for x in rows]
        formatted_rows = "\n".join([build_field_row(*row) for row in split_rows])
        return """Field|Type|Default|Description
        -|-|-|-
        {}""".format(formatted_rows)